package com.shakticoin.app.api.bounty

import retrofit2.Call
import retrofit2.http.*

const val PROMO_URL = "https://www.shakticoin.com/?"

interface BountyService {

    @Headers("Accept: application/json")
    @GET("bounties")
    fun getBounties(@Header("Authorization") authorization: String) : Call<BountyReferralViewModel>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("bounties")
    fun claimBonusBounty(@Header("Authorization") authorization: String,
                                 @Body parameters: GenesisBonusBountyCreateModel) : Call<BountyReferralViewModel?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("bounties/promotioncode/{id}")
    fun promoCode(@Header("Authorization") authorization: String,
                  @Path("id") bonusBountyId: String,
                  @Body parameters: PromotionalCodeModel) : Call<PromotionalCodeResponseViewModel>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("bounties/influence/{id}")
    fun setInfluence(@Header("Authorization") authorization: String,
                     @Path("id") bonusBountyId: String,
                     @Body parameters: InfluenceModel) : Call<CommonReferralResponseViewModel?>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("bounties/grantoptions/{id}")
    fun grantOptions(@Header("Authorization") authorization: String,
                     @Path("id") bonusBountyId: String,
                     @Body parameters: BountyGrantOptionInfoModel) : Call<BountyGrantOptionInfoViewModel>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("bounties/commonreferral/{id}")
    fun addCommonReferral(@Header("Authorization") authorization: String,
                          @Path("id") bonusBountyId: String,
                          @Body parameters: CommonReferralModel) : Call<CommonReferralResponseViewModel>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("bounties/registerreferral/")
    fun registerReferral(@Header("Authorization") authorization: String,
                         @Body parameters: RegisterReferralModel) : Call<RegisterReferralResponseModel>

    @Headers("Accept: application/json")
    @GET("bounties/progressing/{id}")
    fun getProgressingData(@Header("Authorization") authorization: String,
                          @Path("id") bonusBountyId: String) : Call<ProgressingStatusListResponseViewModel?>

    @Headers("Accept: application/json")
    @GET("bounties/effortrate/{id}")
    fun getEffortRate(@Header("Authorization") authorization: String,
                      @Path("id") bonusBountyId: String) : Call<EffortStatusListResponseViewModel?>

    @Headers("Accept: application/json")
    @GET("bounties/generateQR")
    fun generateQR(@Header("Authorization") authorization: String) : Call<QRCodeResponseViewModel>

    @Headers("Accept: application/json")
    @GET("offers/all/")
    fun getOffers(@Header("Authorization") authorization: String) : Call<OfferListResponseViewModel?>
}