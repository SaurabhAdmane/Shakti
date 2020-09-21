package com.shakticoin.app.api.onboard

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OnboardService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("onboardShakti/users")
    fun addUser(@Body parameters: OnboardShaktiModel) : Call<ResponseBean?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("onboardShakti/users/password/change")
    fun changePassword(@Header("Authorization") authorization: String,
                       @Body parameters: UpdatePasswordModel) : Call<ResponseBean?>

    @Headers("Accept: application/json")
    @POST("onboardShakti/wallet")
    fun createWallet(@Header("Authorization") authorization: String?,
                     @Body parameters: WalletRequest) : Call<ResponseBean?>
}