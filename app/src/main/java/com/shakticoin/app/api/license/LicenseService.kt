package com.shakticoin.app.api.license

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LicenseService {

    /** Return all license types  */
    @GET("licenses")
    fun getLicenses(@Header("Authorization") authorization: String?): Call<List<LicenseType>?>

    /** Request all mining license types */
    @GET("licenses/mining")
    fun getMiningLicenses(@Header("Authorization") authorization: String?): Call<ResponseBody?>

    /** Search for license inventories based on region  */
    @GET("licences/inventory/{country}")
    fun getLicenseAvailable(
            @Header("Authorization") authorization: String?,
            @Path("country") country: String?,
            @Query("province") province: String?,
            @Query("city") city: String?): Call<ResponseBody?>?

    @GET("node-operator")
    fun getNodeOperator(@Header("Authorization") authorization: String?): Call<NodeOperatorModel?>

    @PATCH("node-operator")
    fun updateNodeOperator(@Header("Authorization") authorization: String?,
                           @Body parameters: NodeOperatorUpdateModel): Call<NodeOperatorModel?>

    @POST("node-operator/cancellation")
    fun cancelLicence(@Header("Authorization") authorization: String?,
                      @Body parameters: SubscribedLicenseModel) : Call<SubscribedLicenseModel?>


    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("node-operator/checkout/subscription")
    fun checkoutSubscription(@Header("Authorization") authorization: String?,
                 @Body parameters: CheckoutModel) : Call<CheckoutResponse?>
}