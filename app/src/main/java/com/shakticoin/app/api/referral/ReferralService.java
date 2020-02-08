package com.shakticoin.app.api.referral;

import com.shakticoin.app.api.referral.model.Referral;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReferralService {

    /**
     * Create referral.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("referralservice/v1/api/referrals/")
    Call<Referral> addReferral(@Header("Authorization") String authorization, @Body ReferralParameters newReferral);

    /**
     * Retrieve list of referrals.
     */
    @Headers("Accept: application/json")
    @GET("referralservice/v1/api/referrals/")
    Call<Map<String, Object>> getReferrals(@Header("Authorization") String authorization);

    /**
     * Retrieve a referral by ID.
     */
    @Headers("Accept: application/json")
    @GET("referralservice/v1/api/referrals/find_by_referrer/id/{id}")
    Call<Map<String, Object>> findReferralByReferrer(@Header("Authorization") String authorization, @Path("id") String id);
}
