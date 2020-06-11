package com.shakticoin.app.api.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {

    /**
     * Retrieve a user by authorization token
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("users/")
    Call<UserAccount> getUserAccount(@Header("Authorization") String authorization);

    /**
     * Adds a new user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/")
    Call<UserAccount> createUserAccount(@Body CreateUserRequest parameters);

    /**
     * Activate new user.
     * @deprecated
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/auth/activate/")
    Call<Void> activateUser(@Body UserActivateParameters parameters);

    /**
     * Change user password
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/password/change/")
    Call<Void> changePassword(@Header("Authorization") String authorization,
                              @Body PasswordChangeParameters parameters);

    /**
     * Reset user password.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/password/reset/")
    Call<Void> resetPassword(@Body ResetPasswordParameters parameters);
}
