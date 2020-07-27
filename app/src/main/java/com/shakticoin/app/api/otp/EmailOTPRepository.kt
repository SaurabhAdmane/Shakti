package com.shakticoin.app.api.otp

import com.shakticoin.app.api.BackendRepository
import com.shakticoin.app.api.BaseUrl
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.RemoteException
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EmailOTPRepository : BackendRepository() {
    private val http = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    private val service: EmailOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.EMAIL_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(EmailOTPService::class.java)

    val authRepository = AuthRepository();

    fun requestRegistration(email: String, listener: OnCompleteListener<Void?>) {
        val parameters = EmailRegistrationRequest(null, email);
        service.registrationRequest(parameters).enqueue(object : Callback<MainResponseBean?> {
            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body();
                    if (resp != null) {
                        Debug.logDebug(resp.responseMsg + " [" + resp.responseCode + "]")
                        listener.onComplete(null, null)
                    } else listener.onComplete(null, null);
                } else {
                    listener.onComplete(null, RemoteException(
                            getResponseErrorMessage("responseMsg", response.errorBody()), response.code()))
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                returnError(listener, t);
            }
        })
    }

    fun confirmRegistration(token: String, listener: OnCompleteListener<Boolean>) {
        service.confirmRegistration(token).enqueue(object : Callback<MainResponseBean?> {
            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body();
                    if (resp != null) {
                        listener.onComplete(true, null);
                    } else listener.onComplete(null, null);
                } else {
                    return returnError(listener, response);
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
    fun getEmailStatus(email: String) : Boolean? {
        val parameters = EmailStatusRequest()
        parameters.email = email
        try {
            val response: Response<MainResponseBean?> = service.getEmailStatus(parameters).execute();
            if (response.isSuccessful) {
                val resp = response.body()
                //TODO: we need to decode response and return either true or false depending on
                // email verification status
                return true
            } else {
                when (response.code()) {
                    404 -> return false // not found
                    else -> return null
                }
            }
        } catch (e : Exception) {
            Debug.logException(e)
            return null
        }
    }
}