package com.shakticoin.app.api.otp

import com.shakticoin.app.api.BackendRepository
import com.shakticoin.app.api.BaseUrl
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PhoneOTPRepository : BackendRepository() {
    var client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    private val service: PhoneOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.PHONE_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PhoneOTPService::class.java)

    fun requestRegistration(phoneNumber: String, listener: OnCompleteListener<Void>) {
        val parameters = MobileRegistrationRequest();
        parameters.mobileNo = phoneNumber
        service.registrationRequest(parameters).enqueue(object: Callback<MainResponseBean?> {
            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                return returnError(listener, t)
            }

            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        listener.onComplete(null, null)
                    } else listener.onComplete(null, null)
                } else {
                    returnError(listener, response)
                }
            }

        });
    }

    fun confirmRegistration(mobileNo: String, code: String, listener: OnCompleteListener<Boolean>) {
        val parameters = ConfirmRegistrationRequest()
        parameters.mobileNo = mobileNo
        parameters.otp = code
        service.confirmRegistration(parameters).enqueue(object: Callback<MainResponseBean?> {
            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                return returnError(listener, t)
            }

            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        listener.onComplete(true, null)
                    } else listener.onComplete(false, null)
                } else {
                    return returnError(listener, response)
                }
            }

        })
    }
}