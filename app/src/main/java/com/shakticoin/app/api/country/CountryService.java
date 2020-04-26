package com.shakticoin.app.api.country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CountryService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("commonservice/v1/api/countries/")
    Call<List<Country>> getCountries(@Header("Accept-Language") String language);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("commonservice/v1/api/countries/{code}/")
    Call<Country> getCountry(@Header("Accept-Language") String language,
                             @Path("code") String code);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("commonservice/v1/api/countries/{code}/subdivisions/")
    Call<List<Subdivision>> getSubdivisions(@Header("Accept-Language") String language,
                                            @Path("code") String code);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("commonservice/v1/api/countries/{code}/subdivisions/{id}/")
    Call<Subdivision> getSubdivision(@Header("Accept-Language") String language,
                                     @Path("code") String code,
                                     @Path("id") Integer id);
}
