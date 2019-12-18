package com.shakticoin.app.api.user;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    /**
     * Retrieve a user by ID
     */
    @GET("userservice/v1/api/users/{id}/")
    Call<User> getUserByID(
            @Header("Authorization") String authorization, @Path("id") Long id);

    /**
     * Retrieve a user by authorization token
     */
    @GET("userservice/v1/api/users/")
    Call<User> getUser(@Header("Authorization") String authorization);

    /**
     * Adds a new user.
     */
    @POST("userservice/v1/api/users/")
    Call<User> createUser(@Body CreateUserParameters parameters);


    /**
     * Updates a user.
     */
    @PUT("userservice/v1/api/users/{id}/")
    Call<UserResponse> updateUser(
            @Header("Authorization") String authorization,
            @Path("id") Long userId,
            @Body CreateUserParameters parameters);

    /**
     * Activate new user.
     */
    @POST("userservice/v1/api/users/{id}/status/activate")
    Call<Void> activateUser(@Path("id") Long id, @Body UserActivateParameters parameters);
}
