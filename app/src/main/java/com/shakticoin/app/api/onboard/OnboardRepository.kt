package com.shakticoin.app.api.onboard

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.shakticoin.app.R
import com.shakticoin.app.ShaktiApplication
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
    private val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    private val onboardService: OnboardService = Retrofit.Builder()
            .baseUrl(BaseUrl.ONBOARD_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OnboardService::class.java)
    private val authRepository: AuthRepository = AuthRepository()

    private var callAddUsr : Call<ResponseBean?>? = null
    fun addUser(emailAddress: String, countryCode: String, phoneNumber: String, password: String, listener: OnCompleteListener<Void>) {
        addUser(emailAddress, countryCode, phoneNumber, password, listener, false)
    }
    private fun addUser(emailAddress: String, countryCode: String, phoneNumber: String,
                        password: String, listener: OnCompleteListener<Void>, hasRecover401: Boolean) {
        val parameters = OnboardShaktiModel()
        parameters.email = emailAddress
        parameters.countryCode = countryCode
        parameters.mobileNo = phoneNumber
        parameters.password = password
        callAddUsr = onboardService.addUser(parameters)
        callAddUsr!!.enqueue(object: Callback<ResponseBean?> {
            override fun onFailure(call: Call<ResponseBean?>, t: Throwable) {
                return returnError(listener, t)
            }

            override fun onResponse(call: Call<ResponseBean?>, response: Response<ResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        listener.onComplete(null, null)
                    } else listener.onComplete(null, null)
                } else {
                    val context = ShaktiApplication.getContext()
                    when(response.code()) {
                        409 -> listener.onComplete(null, RemoteException(getResponseErrorMessage("details", response.errorBody()), response.code()))
                        410 -> listener.onComplete(null, RemoteException(getResponseErrorMessage("details", response.errorBody()), response.code()))
                        404 -> listener.onComplete(null, RemoteException(context.getString(R.string.reg__pwd_err_not_verified), response.code()))
                        400, 500 -> listener.onComplete(null, RemoteException(context.getString(R.string.reg__pwd_err_unexpected), response.code()))
                        else -> return returnError(listener, response)
                    }
                }
            }

        })
    }

    private var callWlt : Call<ResponseBean?>? = null
    fun createWallet(passphrase: String, listener: OnCompleteListener<String>) {createWallet(passphrase, listener, false)}
    private fun createWallet(passphrase: String, listener: OnCompleteListener<String>, hasRecover401: Boolean) {
        val parameters = WalletRequest()
        parameters.passphrase = passphrase
        callWlt = onboardService.createWallet(Session.getAuthorizationHeader(), parameters)
        callWlt!!.enqueue(object: Callback<ResponseBean?> {
            override fun onFailure(call: Call<ResponseBean?>, t: Throwable) {
                return returnError(listener, t)
            }

            override fun onResponse(call: Call<ResponseBean?>, response: Response<ResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    // it return success if wallet exists already for the account but w/o wallet bytes
                    if (resp != null) {
                        val details = resp.details
                        val walletBytes = details?.get("walletBytes") as String
                        // This is in fact an error situation in spite of success code 201.
                        // It means that the network knows about the user's wallet but the user
                        // either did not store wallet bytes locally or lost it.
                        if (TextUtils.isEmpty(walletBytes)) {
                            listener.onComplete(null, RemoteException(details["message"] as String, response.code()))
                            return
                        }
                        listener.onComplete(walletBytes, null)
                    } else listener.onComplete(null, null)
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
                                        createWallet(passphrase, listener, true)
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

    override fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        super.setLifecycleOwner(lifecycleOwner)
        authRepository.setLifecycleOwner(lifecycleOwner)
    }

    override fun onStop() {
        if (callAddUsr != null && !callAddUsr!!.isCanceled) callAddUsr?.cancel()
        if (callWlt != null && !callWlt!!.isCanceled) callWlt?.cancel()
    }
}