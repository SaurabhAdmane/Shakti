package com.shakticoin.app.api.wallet;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WalletService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/wallets/")
    Call<Map<String, Object>> createWallet(@Header("Authorization") String authorization,
                                           @Body CreateWalletParameters parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/users/wallet/address/")
    Call<Map<String, Object>> getWalletAddress(@Header("Authorization") String authorization,
                                               @Body WalletAddressParameters parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/users/wallet/mybalance/")
    Call<Map<String, Object>> getWalletBalance(@Header("Authorization") String authorization,
                                               @Body WalletBalanceParameters parameters);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("walletservice/api/v1/coins/")
    Call<Map<String, Object>> transferSxeCoins(@Header("Authorization") String authorization,
                                               @Body TransferParameters parameters);
}
