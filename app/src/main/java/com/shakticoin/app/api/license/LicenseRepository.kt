@file:JvmName("LicenceRepository")
package com.shakticoin.app.api.license

import com.shakticoin.app.api.BaseUrl
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.Session
import com.shakticoin.app.util.Debug
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LicenseRepository {
    private val licenseService: LicenseService = Retrofit.Builder()
            .baseUrl(BaseUrl.LICENSESERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LicenseService::class.java)

    fun getLicenses(listener: OnCompleteListener<ResponseBody?>?) {
        licenseService.getLicenses(Session.getAuthorizationHeader())!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val body = response.body()!!.string()
                    Debug.logDebug(body)
                } else {
                    val errorBody = response.errorBody()!!.string()
                    Debug.logDebug(errorBody)
                }
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Debug.logDebug(t.message);
            }
        })
    }

}