package com.shakticoin.app.api.otp

import com.shakticoin.app.BuildConfig
import com.shakticoin.app.R
import com.shakticoin.app.ShaktiApplication
import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EmailOTPRepository : BackendRepository() {
    private val userName = "apiTest"
    private val password = "test"
    var httpLoggingInterceptor = HttpLoggingInterceptor()
    private val http = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            if (BuildConfig.DEBUG) httpLoggingInterceptor.setLevel(
                HttpLoggingInterceptor.Level.BODY
            ) else httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        )
        .build()

    private val service: EmailOTPService = Retrofit.Builder()
        .baseUrl(BaseUrl.EMAIL_OTP_SERVICE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(http)
        .build()
        .create(EmailOTPService::class.java)

    val authRepository = AuthRepository()

    var callReqRegEmail: Call<MainResponseBean?>? = null

    fun getToken(email: String, listener: OnCompleteListener<Void?>) {
        authRepository.login(
            userName,
            password,
            false,
            object : OnCompleteListener<TokenResponse>() {
                override fun onComplete(value: TokenResponse?, error: Throwable?) {
                    val parameters = EmailRegistrationRequest(email = email)
                    callReqRegEmail =
                        service.registrationRequest(Session.getAuthorizationHeader(), parameters)
                    callReqRegEmail!!.enqueue(object : Callback<MainResponseBean?> {
                        override fun onResponse(
                            call: Call<MainResponseBean?>,
                            response: Response<MainResponseBean?>
                        ) {
                            Debug.logDebug(response.toString())
                            if (response.isSuccessful) {
                                val resp = response.body()
                                if (resp != null) {
                                    Debug.logDebug(resp.responseMsg + " [" + resp.responseCode + "]")
                                    listener.onComplete(null, null)
                                } else listener.onComplete(null, null)
                            } else {
                                val context = ShaktiApplication.getContext()
                                var errMsg = context.getString(R.string.err_unexpected)
                                when (response.code()) {
                                    400, 500 -> errMsg =
                                        context.getString(R.string.reg__email_err_unexpected)
                                    404 -> errMsg =
                                        context.getString(R.string.reg__email_err_try_later)
                                    406 -> errMsg =
                                        context.getString(R.string.reg__email_err_blacklisted)
                                    409 -> errMsg =
                                        context.getString(R.string.reg__email_err_registered, email)
                                    422 -> errMsg =
                                        context.getString(R.string.reg__email_err_verified)
                                    429 -> errMsg =
                                        context.getString(R.string.reg__email_err_too_many_attempts)
                                }
                                listener.onComplete(null, RemoteException(errMsg, response.code()))
                            }
                        }

                        override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                            Debug.logDebug(t.message)
                            returnError(listener, t)
                        }
                    })
                }
            })
    }

    fun requestRegistration(email: String, listener: OnCompleteListener<Void?>) {
        if (callReqRegEmail != null && !callReqRegEmail!!.isCanceled) callReqRegEmail?.cancel()
        getToken(email, listener)
    }

    var callConfReg: Call<MainResponseBean?>? = null
    fun confirmRegistration(token: String, listener: OnCompleteListener<Boolean>) {
        callConfReg = service.confirmRegistration(token)
        callConfReg!!.enqueue(object : Callback<MainResponseBean?> {
            override fun onResponse(
                call: Call<MainResponseBean?>,
                response: Response<MainResponseBean?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        listener.onComplete(true, null)
                    } else listener.onComplete(null, null)
                } else {
                    return returnError(listener, response)
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                returnError(listener, t)
            }
        })
    }

    /**
     * Checking an email address verification status synchronously. This method cannot be executed
     * on the main thread.
     */
    fun getEmailStatus(email: String): Boolean? {
        val parameters = EmailStatusRequest()
        parameters.email = email
        try {
            val response: Response<MainResponseBean?> =
                service.getEmailStatus(Session.getAuthorizationHeader(), parameters).execute()
            Debug.logDebug(response.toString())
            if (response.isSuccessful) {
                return true
            } else {
                when (response.code()) {
                    404 -> return false // not found
                    410 -> return false // expired
                    422 -> return true // already verified
                    401 -> {
                        authRepository.refreshToken(
                            Session.getRefreshToken(),
                            object : OnCompleteListener<TokenResponse?>() {
                                override fun onComplete(
                                    value: TokenResponse?,
                                    error: Throwable
                                ) {
                                    getEmailStatus(email)
                                }
                            })
                        return false
                    }
                    else -> return null
                }
            }
        } catch (e: Exception) {
            Debug.logException(e)
            return false
        }
    }

    var callCheckEmailStatus: Call<MainResponseBean?>? = null

    /**
     * Verifies OTP status of an email address.
     * The same as getEmailStatus but asynchronous.
     */
    fun checkEmailStatus(email: String, listener: OnCompleteListener<Boolean?>) {
        val parameters = EmailStatusRequest()
        parameters.email = email
        callCheckEmailStatus = service.getEmailStatus(Session.getAuthorizationHeader(), parameters)
        callCheckEmailStatus!!.enqueue(object : Callback<MainResponseBean?> {
            override fun onResponse(
                call: Call<MainResponseBean?>,
                response: Response<MainResponseBean?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(true, null)
                } else {
                    when (response.code()) {
                        404 -> listener.onComplete(false, null) // not found
                        410 -> listener.onComplete(false, null) // expired
                        401 -> {
                            authRepository.refreshToken(
                                Session.getRefreshToken(),
                                object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(
                                        value: TokenResponse?,
                                        error: Throwable
                                    ) {
                                        if (error != null) {
                                            listener.onComplete(
                                                null,
                                                UnauthorizedException()
                                            )
                                            return
                                        }
                                        checkEmailStatus(email, listener)
                                    }
                                })
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                return returnError(listener, t)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        if (callCheckEmailStatus != null && !callCheckEmailStatus!!.isCanceled) {
            callCheckEmailStatus?.cancel()
        }
        if (callReqRegEmail != null && !callReqRegEmail!!.isCanceled) {
            callReqRegEmail?.cancel()
        }
        if (callConfReg != null && !callConfReg!!.isCanceled) {
            callConfReg?.cancel()
        }
    }
}