package com.shakticoin.app.api.onboard

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

class OnboardRepository : BackendRepository() {
    var client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    private val onboardService: OnboardService = Retrofit.Builder()
            .baseUrl(BaseUrl.ONBOARD_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OnboardService::class.java)
    private val authRepository: AuthRepository = AuthRepository()

    fun addUser(emailAddress: String, phoneNumber: String, password: String, listener: OnCompleteListener<String?>) {
        addUser(emailAddress, phoneNumber, password, listener, false)
    }
    fun addUser(emailAddress: String, phoneNumber: String, password: String, listener: OnCompleteListener<String?>, hasRecover401: Boolean) {
        val parameters: OnboardShaktiUserModel = OnboardShaktiUserModel()
        parameters.email = emailAddress
        parameters.mobileNo = phoneNumber
        parameters.password = password
        onboardService.addUser(parameters).enqueue(object: Callback<ResponseBean?> {
            override fun onFailure(call: Call<ResponseBean?>, t: Throwable) {
                return returnError(listener, t);
            }

            override fun onResponse(call: Call<ResponseBean?>, response: Response<ResponseBean?>) {
                if (response.isSuccessful) {
                    val resp = response.body();
                    if (resp != null) {
                        listener.onComplete(resp.details?.get("shaktiID") as String, null)
                    } else listener.onComplete(null, null);
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object: OnCompleteListener<TokenResponse>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        addUser(emailAddress, phoneNumber, password, listener, true);
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

        })
    }

    fun createWallet(listener: OnCompleteListener<String>) {createWallet(listener, false)}
    fun createWallet(listener: OnCompleteListener<String>, hasRecover401: Boolean) {
        onboardService.createWallet(Session.getAuthorizationHeader()).enqueue(object: Callback<ResponseBean?> {
            override fun onFailure(call: Call<ResponseBean?>, t: Throwable) {
                return returnError(listener, t);
            }

            override fun onResponse(call: Call<ResponseBean?>, response: Response<ResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    Debug.logDebug("ok");
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object: OnCompleteListener<TokenResponse>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        createWallet(listener, true);
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

        })
    }
}