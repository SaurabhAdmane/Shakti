package com.shakticoin.app.api.tier;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TierService {

    @GET("v1/mobile/tier")
    Call<List<Tier>> getTiers(@Header("Authorization") String authorization, @Query("country") String countryCode);
}
