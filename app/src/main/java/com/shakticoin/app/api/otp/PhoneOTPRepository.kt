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
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()

    private val service: PhoneOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.PHONE_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PhoneOTPService::class.java)

    private var callReqReg : Call<MainResponseBean?>? = null
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

    private var callConfReg : Call<MainResponseBean?>? = null
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
                    var msg: String? = getResponseErrorMessage("responseMsg", response.errorBody())
                    if (msg == null) msg = ShaktiApplication.getContext().getString(R.string.err_unexpected)
                    when (response.code()) {
                        409 -> {
                            val context = ShaktiApplication.getContext()
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            listener.onComplete(true, null)
                        } // verified already
                        else -> listener.onComplete(null, RemoteMessageException(msg?:response.message(), response.code()))
                    }
                }
            }

        })
    }

    private var callInquiryNum : Call<MainResponseBean?>? = null
    fun checkPhoneNumberStatus(phoneNumber: String, listener: OnCompleteListener<Boolean>) {
        val parameters = MobileRegistrationRequest()
        parameters.mobileNo = phoneNumber;
        callInquiryNum = service.inquiryPhoneNumber(parameters)
        callInquiryNum?.enqueue(object: Callback<MainResponseBean?> {
            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(true, null)
                } else {
                    when(response.code()) {
                        404 -> listener.onComplete(false, null) // not found
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                return returnError(listener, t)
            }

        })
    }

    private var callCntryCodes : Call<Map<String, String>?>? = null
    fun getCountryCodes(listener : OnCompleteListener<List<IntlPhoneCountryCode>>) {
        callCntryCodes = service.countryCodes()
        callCntryCodes!!.enqueue(object : Callback<Map<String, String>?> {
            override fun onResponse(call: Call<Map<String, String>?>, response: Response<Map<String, String>?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        val list = ArrayList<IntlPhoneCountryCode>()
                        resp.forEach{ (country, code) -> list.add(IntlPhoneCountryCode(code, country)) }
                        list.sortBy { it.countryName }
                    }
                } else {
                    returnError(listener, response)
                }
            }

            override fun onFailure(call: Call<Map<String, String>?>, t: Throwable) {
                returnError(listener, t);
            }
        })
    }

    override fun onStop() {
        super.onStop()
        if (callReqReg != null && !callReqReg!!.isCanceled) callReqReg?.cancel()
        if (callConfReg != null && !callConfReg!!.isCanceled) callConfReg?.cancel()
        if (callInquiryNum != null && !callInquiryNum!!.isCanceled) callInquiryNum?.cancel()
        if (callCntryCodes != null && !callCntryCodes!!.isCanceled) callCntryCodes?.cancel()
    }
}
