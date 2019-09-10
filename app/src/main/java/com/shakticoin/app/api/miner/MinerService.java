package com.shakticoin.app.api.miner;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MinerService {

    @GET("v1/mobile/miner")
    Call<MinerDataResponse> getUserInfo(@Header("Authorization") String authorization);

    @POST("v1/mobile/miner/")
    Call<MinerDataResponse> createUser(@Body CreateUserRequest request);

    @PUT("v1/mobile/miner/{id}/")
    Call<ResponseBody> updateRegStatus(
            @Header("Authorization") String authorization,
            @Path("id") String userId,
            @Body CreateUserRequest request);

    @PATCH("v1/mobile/miner/{user}/")
    Call<ResponseBody> updateMiner(
            @Header("Authorization") String authorization,
            @Path("user") String userId,
            @Body RequestBody request);
}