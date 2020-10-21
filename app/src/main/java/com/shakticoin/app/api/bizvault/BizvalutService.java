package com.shakticoin.app.api.bizvault;

import com.shakticoin.app.api.onboard.ResponseBean;
import com.shakticoin.app.api.wallet.BlockByTimeRequest;
import com.shakticoin.app.api.wallet.BlockByTimeResponse;
import com.shakticoin.app.api.wallet.CoinModel;
import com.shakticoin.app.api.wallet.SessionModelRequest;
import com.shakticoin.app.api.wallet.SessionModelResponse;
import com.shakticoin.app.api.wallet.TransactionInformation;
import com.shakticoin.app.api.wallet.TransferModelResponse;
import com.shakticoin.app.api.wallet.WalletAddressModelRequest;
import com.shakticoin.app.api.wallet.WalletAddressModelResponse;
import com.shakticoin.app.api.wallet.WalletBalanceModelRequest;
import com.shakticoin.app.api.wallet.WalletBalanceModelResponse;
import com.shakticoin.app.api.wallet.WalletModelRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BizvalutService {
    @GET("verify/bizvaultid/")
    Call<ResponseBean> bizvaultStatus(@Header("Authorization") String authorization);

}
