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

    @POST("v1/mobile/password-reset/")
    Call<ResponseBody> reset(@Body PasswordResetRequest request);

    @POST("v1/mobile/check-email-phone/")
    Call<ResponseBody> checkEmailPhone(@Body CheckEmailPhoneParams params);
}
