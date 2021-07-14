package com.shakticoin.app.api.otp

import retrofit2.Call
import retrofit2.http.*

interface PhoneOTPService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("otp/request")
    fun request(@Header("Authorization") authorization: String?,
                @Body parameters : PhoneOTPRequest) : Call<MainResponseBean?>?

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("otp/verify")
    fun verify(@Header("Authorization") authorization: String?,
               @Body parameters : PhoneOTPVerifyRequest) : Call<MainResponseBean?>?

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("registration/request")
    fun registrationRequest(@Header("Authorization") authorization: String?,
                            @Body parameters : MobileRegistrationRequest) : Call<MainResponseBean?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("registration/confirm")
    fun confirmRegistration(@Header("Authorization") authorization: String?,
                            @Body parameters : ConfirmRegistrationRequest) : Call<MainResponseBean?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("inquiry/mobile")
    fun inquiryPhoneNumber(@Header("Authorization") authorization: String?,
                           @Body parameters: MobileRegistrationRequest) : Call<MainResponseBean?>

    @Headers("Accept: application/json")
    @GET("mobile/country-codes")
    fun countryCodes() : Call<List<IntlPhoneCountryCode>?>
}