package com.shakticoin.app.api.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("userservice/v1/api/users/{id}/")
    Call<CreateUserResponse> getUserByID(
            @Header("Authorization") String authorization, @Path("id") Long id);

    @POST("userservice/v1/api/users/")
    Call<CreateUserResponse> createUser(@Body CreateUserParameters parameters);


    @PATCH("userservice/v1/api/users/{id}/")
    Call<CreateUserResponse> updateUser(
            @Header("Authorization") String authorization,
            @Path("user") String userId,
            @Body CreateUserParameters parameters);
}
