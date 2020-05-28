package com.shakticoin.app.api.license

import com.shakticoin.app.api.BaseUrl
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.Session
import com.shakticoin.app.api.auth.AuthRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repo {
    private val licenseService: LicenseService
    private val authRepository: AuthRepository
    fun getLicenses(listener: OnCompleteListener<ResponseBody?>?) {
        licenseService.getLicenses(Session.getAuthorizationHeader())!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {}
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {}
        })
    }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl.WALLETSERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        licenseService = retrofit.create(LicenseService::class.java)
        authRepository = AuthRepository()
    }
}