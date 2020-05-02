package com.shakticoin.app.api.wallet;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WalletService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/users/session/")
    Call<SessionModelResponse> getSession(@Header("Authorization") String authorization,
                                          @Body SessionModelRequest parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/wallets/")
    Call<Map<String, String>> createWallet(@Header("Authorization") String authorization,
                                           @Body WalletModelRequest parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/users/wallet/address/")
    Call<WalletAddressModelResponse> getWalletAddress(@Header("Authorization") String authorization,
                                               @Body WalletAddressModelRequest parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/users/wallet/mybalance/")
    Call<WalletBalanceModelResponse> getWalletBalance(@Header("Authorization") String authorization,
                                               @Body WalletBalanceModelRequest parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/coins/")
    Call<TransferModelResponse> transferSxeCoins(@Header("Authorization") String authorization,
                                               @Body CoinModel parameters);
}
