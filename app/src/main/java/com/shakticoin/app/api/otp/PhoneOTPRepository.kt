package com.shakticoin.app.api.otp

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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
    private var client = OkHttpClient.Builder()
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
    fun requestRegistration(countryCode: String, phoneNumber: String, listener: OnCompleteListener<Void?>) {
        val parameters = MobileRegistrationRequest(countryCode, phoneNumber)
        callReqReg = service.registrationRequest(parameters)
        callReqReg!!.enqueue(object : Callback<MainResponseBean?> {
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
                    val context = ShaktiApplication.getContext()
                    var errMsg = getResponseErrorMessage("responseMsg", response.errorBody())
                    when (response.code()) {
                        409 -> listener.onComplete(null, null) // consider success and allow to proceed
                        400 -> errMsg = context.getString(R.string.reg__mobile_err_recheck)
                        406 -> errMsg = context.getString(R.string.reg__mobile_err_blacklisted)
                        429 -> errMsg = context.getString(R.string.reg__mobile_err_too_many_attempts)
                        500 -> errMsg = context.getString(R.string.reg__mobile_err_unexpected)
                        503 -> errMsg = context.getString(R.string.reg__mobile_err_cannot_send)
                        else -> return returnError(listener, response)
                    }
                    listener.onComplete(null, RemoteException(errMsg, response.code()))
                }
            }

        })
    }

    private var callConfReg : Call<MainResponseBean?>? = null
    fun confirmRegistration(countryCode: String, mobileNo: String, code: String, listener: OnCompleteListener<Boolean?>) {
        val parameters = ConfirmRegistrationRequest(countryCode, mobileNo, code)
        callConfReg = service.confirmRegistration(parameters)
        callConfReg!!.enqueue(object : Callback<MainResponseBean?> {
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
                    val context = ShaktiApplication.getContext()
                    var msg: String? = getResponseErrorMessage("responseMsg", response.errorBody())
                    if (msg == null) msg = ShaktiApplication.getContext().getString(R.string.err_unexpected)
                    when (response.code()) {
                        422 -> {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            listener.onComplete(true, null)
                        } // verified already
                        406 -> msg = context.getString(R.string.reg__mobile_err_code_invalid)
                        410 -> msg = context.getString(R.string.reg__mobile_err_code_expired)
                        429 -> msg = context.getString(R.string.reg__mobile_err_too_many_attempts)
                        500 -> msg = context.getString(R.string.reg__mobile_err_unexpected)
                    }
                    listener.onComplete(null, RemoteMessageException(msg, response.code()))
                }
            }

        })
    }

    private var callInquiryNum : Call<MainResponseBean?>? = null
    fun checkPhoneNumberStatus(countryCode: String, phoneNumber: String, listener: OnCompleteListener<Boolean>) {
        val parameters = MobileRegistrationRequest(countryCode, phoneNumber)
        callInquiryNum = service.inquiryPhoneNumber(parameters)
        callInquiryNum?.enqueue(object : Callback<MainResponseBean?> {
            override fun onResponse(call: Call<MainResponseBean?>, response: Response<MainResponseBean?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(true, null)
                } else {
                    when (response.code()) {
                        404 -> listener.onComplete(false, null) // not found
                        410 -> listener.onComplete(false, null) // OTP expired
                        //410 -> listener.onComplete(null, RemoteException(getResponseErrorMessage("responseMsg", response.errorBody()), response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<MainResponseBean?>, t: Throwable) {
                return returnError(listener, t)
            }

        })
    }

    private var callCntryCodes : Call<List<IntlPhoneCountryCode>?>? = null
    fun getCountryCodes(listener: OnCompleteListener<List<IntlPhoneCountryCode>>) {
        callCntryCodes = service.countryCodes()
        callCntryCodes!!.enqueue(object : Callback<List<IntlPhoneCountryCode>?> {
            override fun onResponse(call: Call<List<IntlPhoneCountryCode>?>, response: Response<List<IntlPhoneCountryCode>?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val list = response.body()
                    if (list != null) {
                        listener.onComplete(list.sortedBy { it.country }, null)
                    }
                } else {
                    returnError(listener, response)
                }
            }

            override fun onFailure(call: Call<List<IntlPhoneCountryCode>?>, t: Throwable) {
                returnError(listener, t)
            }
        })
    }

    fun getCountryCodeList() : MutableLiveData<List<IntlPhoneCountryCode>> {
        val liveData = MutableLiveData<List<IntlPhoneCountryCode>>()

        getCountryCodes(object : OnCompleteListener<List<IntlPhoneCountryCode>>() {
            override fun onComplete(value: List<IntlPhoneCountryCode>?, error: Throwable?) {
                if (error != null) return
                liveData.value = value
            }

        })
        return liveData
    }

    override fun onStop() {
        super.onStop()
        if (callReqReg != null && !callReqReg!!.isCanceled) callReqReg?.cancel()
        if (callConfReg != null && !callConfReg!!.isCanceled) callConfReg?.cancel()
        if (callInquiryNum != null && !callInquiryNum!!.isCanceled) callInquiryNum?.cancel()
        if (callCntryCodes != null && !callCntryCodes!!.isCanceled) callCntryCodes?.cancel()
    }
}
