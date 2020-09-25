package com.shakticoin.app.api.license

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.api.country.CountryRepository
import com.shakticoin.app.api.country.Subdivision
import com.shakticoin.app.api.kyc.KYCRepository
import com.shakticoin.app.api.kyc.KycUserView
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LicenseRepository : BackendRepository() {

    private val http = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()

    private val licenseService: LicenseService = Retrofit.Builder()
            .baseUrl(BaseUrl.LICENSESERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(LicenseService::class.java)

    private val authRepository = AuthRepository()
    private val kycRepository = KYCRepository()
    private val countryRepository = CountryRepository()

    private var callLics : Call<List<LicenseType>?>? = null
    fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?) {
        getLicenses(listener, false)
    }
    private fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?, hasRecover401: Boolean = false) {
        callLics = licenseService.getLicenses(Session.getAuthorizationHeader())
        callLics!!.enqueue(object : Callback<List<LicenseType>?> {
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

    private var callGetInv : Call<ResponseBody?>? = null
    fun getInventory(country: String, stateProvince: String?, city: String?, listener: OnCompleteListener<List<Map<String, Any>>>) {
        getInventory(country, stateProvince, city, listener, false)
    }
    private fun getInventory(country: String, stateProvince: String?, city: String?, listener: OnCompleteListener<List<Map<String, Any>>>, hasRecover401: Boolean) {
        callGetInv = licenseService.getLicenseAvailable(Session.getAuthorizationHeader(), country, stateProvince, city)
        callGetInv!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    Debug.logDebug(body);

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getInventory(country, stateProvince, city, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                returnError(listener, t)
            }

        })
    }

    private var callNodeOp : Call<NodeOperatorModel?>? = null
    fun getNodeOperator(listener: OnCompleteListener<NodeOperatorModel?>) {
        getNodeOperator(listener, false)
    }
    private fun getNodeOperator(listener: OnCompleteListener<NodeOperatorModel?>, hasRecover401: Boolean) {
        callNodeOp = licenseService.getNodeOperator(Session.getAuthorizationHeader())
        callNodeOp!!.enqueue(object: Callback<NodeOperatorModel?> {
            override fun onResponse(call: Call<NodeOperatorModel?>, response: Response<NodeOperatorModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
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
                                        getNodeOperator(listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<NodeOperatorModel?>, t: Throwable) {
                return returnError(listener, t)
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
                    listener.onComplete(null, error)
                    return
                }

                val countryCode = user?.address?.countryCode
                val stateProvinceCode = user?.address?.stateProvinceCode

                countryRepository.getSubdivisionsByCountry(countryCode!!, object : OnCompleteListener<List<Subdivision>>() {
                    override fun onComplete(stateProvinceList: List<Subdivision>?, error: Throwable?) {
                        if (error != null) {
                            listener.onComplete(null, error)
                            return
                        }

                        var stateProvinceName : String? = null
                        if (stateProvinceCode != null && stateProvinceList != null && stateProvinceList.isNotEmpty()) {
                            for (stateProvince in stateProvinceList) {
                                if (stateProvinceCode == stateProvince.subdivision) {
                                    stateProvinceName = stateProvince.name
                                    break
                                }
                            }
                        }

                        val parameters = CheckoutModel()
                        parameters.planCode = planCode
                        val address = AddressModel()
                        address.countryCode = countryCode
                        // Assign the country name to the country code as a workaround for inventory check.
                        address.country = countryCode
                        address.province = stateProvinceName?:stateProvinceCode
                        address.city = user.address?.city
                        address.street = user.address?.address1
                        if (user.address?.address2 != null) address.street += (" " + user.address?.address2)
                        address.zipCode = user.address?.zipCode
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
        })
    }

    private var callUpgdSub : Call<CheckoutResponse?>? = null
    fun upgradeSubscription(planCode: String, subscriptionId: String, listener: OnCompleteListener<String>) {
        upgradeSubscription(planCode, subscriptionId, listener, false)
    }
    private fun upgradeSubscription(planCode: String, subscriptionId: String, listener: OnCompleteListener<String>, hasRecover401: Boolean) {
        val parameters = CheckoutPlanRequest()
        parameters.planCode = planCode
        parameters.subscriptionId = subscriptionId
        callUpgdSub = licenseService.checkoutUpgrade(Session.getAuthorizationHeader(), parameters)
        callUpgdSub!!.enqueue(object : Callback<CheckoutResponse?> {
            override fun onResponse(call: Call<CheckoutResponse?>, response: Response<CheckoutResponse?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) Debug.logDebug(resp.message)
                    listener.onComplete(resp?.hostedpage, null)

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        upgradeSubscription(planCode, subscriptionId, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        404 -> listener.onComplete(null, RemoteException(getResponseErrorMessage("message", response.errorBody()), response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                return returnError(listener, t)
            }
        })
    }

    private var callSubDowngd : Call<CheckoutResponse?>? = null
    fun downgradeSubscription(planCode: String, subscriptionId: String, listener: OnCompleteListener<String>) {
        downgradeSubscription(planCode, subscriptionId, listener, false)
    }
    private fun downgradeSubscription(planCode: String, subscriptionId: String, listener: OnCompleteListener<String>, hasRecover401: Boolean) {
        val parameters = CheckoutPlanRequest()
        parameters.planCode = planCode
        parameters.subscriptionId = subscriptionId
        callSubDowngd = licenseService.checkoutDowngrade(Session.getAuthorizationHeader(), parameters)
        callSubDowngd!!.enqueue(object : Callback<CheckoutResponse?> {
            override fun onResponse(call: Call<CheckoutResponse?>, response: Response<CheckoutResponse?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) Debug.logDebug(resp.message)
                    listener.onComplete(resp?.hostedpage, null)

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        downgradeSubscription(planCode, subscriptionId, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        404 -> listener.onComplete(null, RemoteException(getResponseErrorMessage("message", response.errorBody()), response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                return returnError(listener, t)
            }
        })
    }

    fun searchGeo(country: String?, province: String?) : LiveData<List<Map<String, Object>>> {
        return searchGeo(country, province, false)
    }
    private fun searchGeo(country: String?, province: String?, hasRecover401: Boolean) : LiveData<List<Map<String, Object>>> {
        val geoList = MutableLiveData<List<Map<String, Object>>>()

        licenseService.searchGeo(Session.getAuthorizationHeader(), country, province).enqueue(object : Callback<ResponseBody?>{
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    Debug.logDebug(body)
                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            return
                                        }
                                        searchGeo(country, province, true)
                                    }
                                })
                            } else {
                                Debug.logException(UnauthorizedException())
                                return
                            }
                        }
                        else -> {
                            Debug.logErrorResponse(response)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Debug.logException(t);
            }
        })

        return geoList
    }

    override fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        super.setLifecycleOwner(lifecycleOwner)
        authRepository.setLifecycleOwner(lifecycleOwner)
        kycRepository.setLifecycleOwner(lifecycleOwner)
        countryRepository.setLifecycleOwner(lifecycleOwner)
    }

    override fun onStop() {
        super.onStop()
        if (callLics != null && !callLics!!.isCanceled) callLics?.cancel()
        if (callNodeOp != null && !callNodeOp!!.isCanceled) callNodeOp?.cancel()
        if (callSubDowngd != null && !callSubDowngd!!.isCanceled) callSubDowngd?.cancel()
        if (callUpgdSub != null && !callUpgdSub!!.isCanceled) callUpgdSub?.cancel()
    }
}