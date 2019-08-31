package com.shakticoin.app.api.country;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface CountryService {
    @GET("v1/mobile/country/")
    Call<Map<String, String>> getCountries(@Header("Accept-Language") String language);
}
