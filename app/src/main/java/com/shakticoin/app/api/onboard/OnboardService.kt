package com.shakticoin.app.api.onboard

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OnboardService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("onboardShakti/users")
    fun addUser(@Body parameters: OnboardShaktiUserModel) : Call<ResponseBean?>

    @Headers("Accept: application/json")
    @POST("onboardShakti/wallet")
    fun saveWallet(@Header("Authorization") authorization: String?) : Call<ResponseBean?>
}