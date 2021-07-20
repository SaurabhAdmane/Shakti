package com.shakticoin.app.api.onboard

import com.shakticoin.app.api.Session
import retrofit2.Call
import retrofit2.http.*

interface OnboardService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users")
    fun addUser(@Header("Authorization") authorization: String? = Session.getAuthorizationHeader(),
                @Body parameters: OnboardShaktiModel) : Call<ResponseBean?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("onboardShakti/users/password/change")
    fun changePassword(@Header("Authorization") authorization: String,
                       @Body parameters: UpdatePasswordModel) : Call<ResponseBean?>
    @Headers("Accept: application/json")
    @POST("onboardShakti/wallet")
    fun createWallet(@Header("Authorization") authorization: String?,
                     @Body parameters: WalletRequest) : Call<ResponseBean?>


    @Headers("Accept: application/json")
    @GET("onboardShakti/users/password/status")
    fun getPasswordRecStatus(@Header("Authorization") authorization: String?) : Call<ResponseBean?>







}