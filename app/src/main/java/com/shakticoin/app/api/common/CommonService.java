package com.shakticoin.app.api.common;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface CommonService {

    @Headers("Accept: application/json")
    @GET("/commonservice/v1/api/contacts/reasons/")
    Call<List<RequestReason>> contactReasons(@Header("Authorization") String authorization,
                                             @Header("Accept-Language") String language);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/commonservice/v1/api/contacts/")
    Call<ContactUs> sendMessage(@Header("Authorization") String authorization, @Body ContactUs newMessage);
}
