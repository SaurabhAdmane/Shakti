package com.shakticoin.app.api.referrals;

import com.shakticoin.app.api.onboard.ResponseBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReferralService {
    @GET("bounties/")
    Call<ResponseBean> bountyStatus(@Header("Authorization") String authorization);

}
