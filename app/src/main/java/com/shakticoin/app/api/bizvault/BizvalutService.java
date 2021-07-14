package com.shakticoin.app.api.bizvault;

import com.shakticoin.app.api.onboard.ResponseVault;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface BizvalutService {
    @GET("bizvaults/verify/bizvaultid")
    Call<ResponseVault> bizvaultStatus(@Header("Authorization") String authorization);
}
