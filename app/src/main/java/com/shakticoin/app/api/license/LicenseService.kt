package com.shakticoin.app.api.license

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface LicenseService {

    /** Return all license types  */
    @GET("licenses")
    fun getLicenses(@Header("Authorization") authorization: String?): Call<ResponseBody?>?

    /** Search for license inventories based on region  */
    @GET("licences/inventory/{country}")
    fun getLicenseAvailable(
            @Header("Authorization") authorization: String?,
            @Path("country") country: String?,
            @Query("province") province: String?,
            @Query("city") city: String?): Call<ResponseBody?>?
}