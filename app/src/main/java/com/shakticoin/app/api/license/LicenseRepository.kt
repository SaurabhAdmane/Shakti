@file:JvmName("LicenceRepository")
package com.shakticoin.app.api.license

import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val MINING_PLANS: List<String> = listOf("M101W", "T100W", "T200W", "T300W", "T400W",
        "M101M", "T100M", "T200M", "T300M", "T400M", "M101Y", "T100Y", "T200Y", "T300Y", "T400Y")

class LicenseRepository {

    private val http = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    private val licenseService: LicenseService = Retrofit.Builder()
            .baseUrl(BaseUrl.LICENSESERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(LicenseService::class.java)

    val authRepository = AuthRepository();

    fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?) {
        getLicenses(listener, false)
    }
    fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?, hasRecover401: Boolean = false) {
        licenseService.getLicenses(Session.getAuthorizationHeader()).enqueue(object : Callback<List<LicenseType>?> {
            override fun onResponse(call: Call<List<LicenseType>?>, response: Response<List<LicenseType>?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val licenseTypes = response.body();
                    if (licenseTypes != null) {
                        // good to have them ordered properly
                        listener!!.onComplete(licenseTypes.sortedBy { it.orderNumber }, null)
                    } else listener!!.onComplete(listOf(), null);
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                    if (error != null) {
                                        listener!!.onComplete(null, error)
                                        return;
                                    }
                                    getLicenses(listener, true)
                                }
                            })
                        } else {
                            listener!!.onComplete(null, UnauthorizedException());
                            return;
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

}