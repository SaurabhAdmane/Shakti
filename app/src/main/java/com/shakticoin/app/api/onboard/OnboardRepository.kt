package com.shakticoin.app.api.onboard

import com.shakticoin.app.api.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnboardRepository {
    private val onboardService: OnboardService = Retrofit.Builder()
            .baseUrl(BaseUrl.ONBOARD_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnboardService::class.java)

}