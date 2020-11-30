package com.shakticoin.app.api.referrals;

import com.shakticoin.app.api.onboard.ResponseBean;
import com.shakticoin.app.api.referral.ReferralParameters;
import com.shakticoin.app.api.referral.model.Referral;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ReferralService {
    @GET("bounties/")
    Call<ResponseBean> bountyStatus(@Header("Authorization") String authorization);


    /**
     * Create referral.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("bounties/registerreferral/")
    Call<Referral> getReferral(@Header("Authorization") String authorization, @Body ReferralParameters newReferral);

}
