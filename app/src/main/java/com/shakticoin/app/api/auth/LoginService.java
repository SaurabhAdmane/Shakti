package com.shakticoin.app.api.auth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {
    @POST("userservice/v1/api/token/")
    Call<TokenResponse> token(@Body Credentials credentials);

    @POST("userservice/v1/api/token/refresh/")
    Call<TokenResponse> refresh(@Body TokenParameters parameters);

    @POST("/userservice/v1/api/token/verify/")
    Call<ResponseBody> verify(@Body PasswordResetRequest request);
}
