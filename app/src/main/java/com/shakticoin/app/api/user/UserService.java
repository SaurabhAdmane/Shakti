package com.shakticoin.app.api.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    /**
     * Retrieve a user by ID
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("userservice/v1/api/users/{id}/")
    Call<User> getUserByID(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer userId);

    /**
     * Retrieve a user by authorization token
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("userservice/v1/api/users/")
    Call<User> getUser(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language);

    /**
     * Adds a new user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/users/")
    Call<User> createUser(@Body CreateUserParameters parameters);


    /**
     * Updates a user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("userservice/v1/api/users/{id}/")
    Call<UserResponse> updateUser(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer userId,
            @Body CreateUserParameters parameters);

    /** Retrieves user address by ID */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("userservice/v1/api/users/{id}/address/")
    Call<Residence> getUserAddress(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer userId);

    /**
     * Activate new user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/users/{id}/status/activate/")
    Call<Void> activateUser(@Path("id") Integer userId, @Body UserActivateParameters parameters);

    /**
     * Change user password
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/users/{id}/password/change/")
    Call<Void> changePassword(@Header("Authorization") String authorization, @Path("id") Integer userId);

    /**
     * Reset user password.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/users/password/reset/")
    Call<Void> resetPassword(@Body ResetPasswordParameters parameters);

    /**
     * Confirm user password reset.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userservice/v1/api/users/password/reset/confirm/")
    Call<Void> confirmResetPassword(@Body ConfirmResetPasswordParameters parameters);


    /**
     * Retrieve family members.
     */
    @Headers("Accept: application/json")
    @GET("/userservice/v1/api/families/")
    Call<List<FamilyMember>> getFamilyMembers(@Header("Authorization") String authorization,
                                              @Header("Accept-Language") String language);

    /**
     * Add a family member.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<FamilyMember> addFamilyMember(@Header("Authorization") String authorization,
                                       @Header("Accept-Language") String language,
                                       @Body FamilyMember member);
}
