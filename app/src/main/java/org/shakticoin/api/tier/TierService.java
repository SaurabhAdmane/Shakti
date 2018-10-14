package org.shakticoin.api.tier;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TierService {

    @GET("v1/mobile/tier")
    Call<ResponseBody> getTiers(@Header("Authorization") String authorization, @Query("country") String countryCode);
}
