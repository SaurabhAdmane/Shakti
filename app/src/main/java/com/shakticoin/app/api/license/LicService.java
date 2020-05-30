package com.shakticoin.app.api.license;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LicService {
    @GET("licenses")
    Call<ResponseBody> getLicenses(@Header("Authorization") String authorization);
}
