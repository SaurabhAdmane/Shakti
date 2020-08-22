package com.shakticoin.app.api.license

import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.api.kyc.KYCRepository
import com.shakticoin.app.api.kyc.KycUserView
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LicenseRepository : BackendRepository() {

    private val http = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    private val licenseService: LicenseService = Retrofit.Builder()
            .baseUrl(BaseUrl.LICENSESERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(LicenseService::class.java)

    private val authRepository = AuthRepository()
    private val kycRepository = KYCRepository()

    fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?) {
        getLicenses(listener, false)
    }
    private fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?, hasRecover401: Boolean = false) {
        licenseService.getLicenses(Session.getAuthorizationHeader()).enqueue(object : Callback<List<LicenseType>?> {
            override fun onResponse(call: Call<List<LicenseType>?>, response: Response<List<LicenseType>?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val licenseTypes = response.body()
                    if (licenseTypes != null) {
                        // good to have them ordered properly
                        listener!!.onComplete(licenseTypes.sortedBy { it.orderNumber }, null)
                    } else listener!!.onComplete(listOf(), null)
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                    if (error != null) {
                                        listener!!.onComplete(null, error)
                                        return
                                    }
                                    getLicenses(listener, true)
                                }
                            })
                        } else {
                            listener!!.onComplete(null, UnauthorizedException())
                            return
                        }
                    } else {
                        Debug.logErrorResponse(response)
                        listener!!.onComplete(null, RemoteException(response.message(), response.code()))
                    }
                }
            }
            override fun onFailure(call: Call<List<LicenseType>?>, t: Throwable) {
                Debug.logException(t)
                listener!!.onComplete(null, t)
            }
        })
    }

    fun checkoutSubscription(planCode: String?, listener: OnCompleteListener<String>) {
        checkoutSubscription(planCode, listener, false)
    }
    private fun checkoutSubscription(planCode: String?, listener: OnCompleteListener<String>, hasRecover401: Boolean) {
        kycRepository.getUserDetails(object: OnCompleteListener<KycUserView>() {
            override fun onComplete(user: KycUserView?, error: Throwable?) {
                if (error != null) {
                    return
                }

                val parameters = CheckoutModel()
                parameters.planCode = planCode
                val address = AddressModel()
                address.countryCode = user?.address?.countryCode
                address.country = user?.address?.country
                address.province = user?.address?.stateProvinceCode
                address.city = user?.address?.city
                address.street = user?.address?.address1
                if (user?.address?.address2 != null) address.street += (" " + user.address?.address2)
                address.zipCode = user?.address?.zipCode
                parameters.address = address
                licenseService.checkoutSubscription(Session.getAuthorizationHeader(), parameters).enqueue(object: Callback<CheckoutResponse?> {
                    override fun onResponse(call: Call<CheckoutResponse?>, response: Response<CheckoutResponse?>) {
                        Debug.logDebug(response.toString())
                        if (response.isSuccessful) {
                            val resp = response.body()
                            if (resp != null) Debug.logDebug(resp.message)
                            listener.onComplete(resp?.hostedpage, null)
                        } else {
                            when(response.code()) {
                                401 -> {
                                    if (!hasRecover401) {
                                        authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                            override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                                if (error != null) {
                                                    listener.onComplete(null, error)
                                                    return
                                                }
                                                checkoutSubscription(planCode, listener, true)
                                            }
                                        })
                                    } else {
                                        listener.onComplete(null, UnauthorizedException())
                                        return
                                    }
                                }
                                else -> return returnError(listener, response)
                            }
                        }
                    }

                    override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                        return returnError(listener, t)
                    }

                })
            }
        })
    }
}