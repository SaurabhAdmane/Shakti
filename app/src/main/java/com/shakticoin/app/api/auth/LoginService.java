package com.shakticoin.app.api.auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept: application/json"})
    @FormUrlEncoded
    @POST("token")
    Call<TokenResponse> token(@Header("Authorization") String auth,
                              @Field("grant_type") String grantType,
                              @Field("scope") String scope,
                              @Field("username") String username,
                              @Field("password") String password);

    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept: application/json"})
    @FormUrlEncoded
    @POST("token")
    Call<TokenResponse> refresh(@Header("Authorization") String auth,
                                @Field("grant_type") String grantType,
                                @Field("refresh_token") String refreshToken);
}
