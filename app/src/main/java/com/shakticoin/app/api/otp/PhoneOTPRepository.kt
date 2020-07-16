package com.shakticoin.app.api.otp

import com.shakticoin.app.api.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhoneOTPRepository {
    private val service: PhoneOTPService = Retrofit.Builder()
            .baseUrl(BaseUrl.PHONE_OTP_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhoneOTPService::class.java)

}