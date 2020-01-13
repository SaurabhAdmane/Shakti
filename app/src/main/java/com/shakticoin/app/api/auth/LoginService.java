package com.shakticoin.app.api.auth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/token/")
    Call<TokenResponse> token(@Body Credentials credentials);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/token/refresh/")
    Call<TokenResponse> refresh(@Body TokenParameters parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/token/verify/")
    Call<ResponseBody> verify(@Body PasswordResetRequest request);
}
