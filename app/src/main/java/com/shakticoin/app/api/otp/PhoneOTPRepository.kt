package com.shakticoin.app.api.otp

import android.widget.Toast
import com.shakticoin.app.R
import com.shakticoin.app.ShaktiApplication
import com.shakticoin.app.api.*
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
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    private val service: PhoneOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.PHONE_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PhoneOTPService::class.java)

    var callReqReg : Call<MainResponseBean?>? = null
    fun requestRegistration(phoneNumber: String, listener: OnCompleteListener<Void?>) {
        val parameters = MobileRegistrationRequest();
        parameters.mobileNo = phoneNumber
        callReqReg = service.registrationRequest(parameters)
        callReqReg!!.enqueue(object: Callback<MainResponseBean?> {
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
                    when(response.code()) {
                        503 -> listener.onComplete(null, RemoteException(
                                getResponseErrorMessage("responseMsg", response.errorBody()), response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

        });
    }

    var callConfReg : Call<MainResponseBean?>? = null
    fun confirmRegistration(mobileNo: String, code: String, listener: OnCompleteListener<Boolean?>) {
        val parameters = ConfirmRegistrationRequest()
        parameters.mobileNo = mobileNo
        parameters.otp = code
        callConfReg = service.confirmRegistration(parameters)
        callConfReg!!.enqueue(object: Callback<MainResponseBean?> {
            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                Debug.logDebug(t.message)
                return returnError(listener, t)
            }

            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp?.responseMsg != null) {
                        val context = ShaktiApplication.getContext()
                        Toast.makeText(context, resp.responseMsg, Toast.LENGTH_LONG).show()
                    }
                    listener.onComplete(true, null)
                } else {
                    var errorMsg: String? = getResponseErrorMessage("responseMsg", response.errorBody())
                    if (errorMsg == null) errorMsg = ShaktiApplication.getContext().getString(R.string.err_unexpected)
                    listener.onComplete(null, RemoteMessageException(errorMsg?:response.message(), response.code()))
                    return;
                }
            }

        })
    }

    override fun onStop() {
        super.onStop()
        if (callReqReg != null && !callReqReg!!.isCanceled) callReqReg?.cancel()
        if (callConfReg != null && !callConfReg!!.isCanceled) callConfReg?.cancel()
    }
}
