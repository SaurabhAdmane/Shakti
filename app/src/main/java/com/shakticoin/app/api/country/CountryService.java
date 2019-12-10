package com.shakticoin.app.api.country;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CountryService {

    @GET("commonservice/v1/api/countries/")
    Call<List<Country>> getCountries(@Header("Accept-Language") String language);
}
