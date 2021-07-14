package com.shakticoin.app.api.otp

import retrofit2.Call
import retrofit2.http.*

interface EmailOTPService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("otp/request")
    fun request(@Header("Authorization") authorization: String?,
                @Body parameters : EmailOTPRequest) : Call<MainResponseBean?>?

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("otp/verify")
    fun verify(@Header("Authorization") authorization: String?,
               @Body parameters : EmailOTPVerifyRequest) : Call<MainResponseBean?>?


    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("registration/request")
    fun registrationRequest(@Header("Authorization") authorization: String?,
                            @Body parameters : EmailRegistrationRequest) : Call<MainResponseBean?>

    @Headers("Accept: application/json")
    @GET("registration/confirm-registration")
    fun confirmRegistration(@Query("token") token : String) : Call<MainResponseBean?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("inquiry/email")
    fun getEmailStatus(@Header("Authorization") authorization: String?,
                       @Body parameters : EmailStatusRequest) : Call<MainResponseBean?>
}