package com.shakticoin.app.api.otp

import com.shakticoin.app.api.BackendRepository
import com.shakticoin.app.api.BaseUrl
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.util.Debug
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmailOTPRepository : BackendRepository() {
    private val service: EmailOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.EMAIL_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmailOTPService::class.java)

    val authRepository = AuthRepository();

    fun requestRegistration(email: String, listener: OnCompleteListener<Void>) {requestRegistration(email, listener)}
    fun requestRegistration(email: String, listener: OnCompleteListener<Void>, hasRecover401: Boolean = false) {
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
                    //TODO: not clear if 401 is possible at this point
                    return returnError(listener, response)
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                returnError(listener, t);
            }
        })
    }

    fun confirmRegistration(token: String, listener: OnCompleteListener<Boolean>) {

    }
}