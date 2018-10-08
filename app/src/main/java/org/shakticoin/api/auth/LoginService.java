package org.shakticoin.api.auth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface LoginService {
    @POST("rest-auth/login/")
    Call<LoginServiceResponse> login(@Body Credentials credentials);

    @POST("rest-auth/logout/")
    Call<ResponseBody> logout(@Header("Authorization") String authorization);

    @POST("rest-auth/password/reset")
    Call<ResponseBody> reset(@Header("Authorization") String authorization, @Body PasswordResetRequest request);
}
