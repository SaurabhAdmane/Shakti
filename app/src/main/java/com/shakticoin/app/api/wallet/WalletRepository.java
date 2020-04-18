package com.shakticoin.app.api.wallet;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class WalletRepository extends BackendRepository {
    private WalletService service;
    private AuthRepository authRepository;

    public WalletRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WalletService.class);
        authRepository = new AuthRepository();
    }

    /**
     * Creates a new wallet and return wallet bytes as a string. Wallet bytes must be stored and
     * used in parameters of other calls.
     * @param passphrase The wallet password. Basically, it's encryption passphrase that is not
     *                   depends on user's account password.
     */
    public void createWallet(@NonNull String passphrase, @NonNull OnCompleteListener<String> listener) {
        CreateWalletParameters parameters = new CreateWalletParameters(null, passphrase);
        service.createWallet(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, String>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, String> results = response.body();
                    if (results != null) {
                        String message = results.get("message");
                        if (message != null) Debug.logDebug(message);
                        String walletBytes = results.get("walletBytes");
                        listener.onComplete(walletBytes, null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                createWallet(passphrase, listener);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getAddress(@NonNull WalletAddressParameters parameters, @NonNull OnCompleteListener<String> listener) {
        service.getWalletAddress(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, String>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, String> results = response.body();
                    if (results != null) {
                        String message = results.get("message");
                        if (message != null) Debug.logDebug(message);
                        String walletAddress = results.get("walletAddress");
                        listener.onComplete(walletAddress, null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getAddress(parameters, listener);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getBalance(@NonNull WalletBalanceParameters parameters, @NonNull OnCompleteListener<BigDecimal> listener) {
        service.getWalletBalance(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, Object> results = response.body();
                    if (results != null) {
                        String message = (String) results.get("message");
                        if (message != null) Debug.logDebug(message);
                        Long walletBalance = (Long) results.get("walletBalance");
                        BigDecimal balance = BigDecimal.ZERO;
                        if (walletBalance != null) {
                            balance = BigDecimal.valueOf(walletBalance);
                        }
                        listener.onComplete(balance, null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getBalance(parameters, listener);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void transfer(TransferParameters parameters, @NonNull OnCompleteListener<String> listener) {
        service.transferSxeCoins(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, String>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, String> results = response.body();
                    if (results != null) {
                        String message = results.get("message");
                        if (message != null) Debug.logDebug(message);
                        String transactionId = results.get("transactionId");
                        listener.onComplete(transactionId, null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                transfer(parameters, listener);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    /**
     * Returns list of transactions on an account
     * // TODO: we should request transactions for a particular wallet but for now it's not clear
     */
    public void getTransactions(@NonNull OnCompleteListener<List<Transaction>> listener) {
        List<Transaction> transactionList = new ArrayList<>();
//
//        transactionList.add(new Transaction("00001", "Transaction Name", new Date(119, 8, 1, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00002", "Transaction Name", new Date(119, 8, 1, 11, 25, 30), new BigDecimal("-12.9")));
//        transactionList.add(new Transaction("00003", "Transaction Name", new Date(119, 8, 4, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00004", "Transaction Name", new Date(119, 8, 11, 11, 23, 30), new BigDecimal("-12.5")));
//        transactionList.add(new Transaction("00005", "Transaction Name", new Date(119, 8, 12, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00006", "Transaction Name", new Date(119, 8, 15, 11, 23, 30), new BigDecimal("-12.5")));
//        transactionList.add(new Transaction("00007", "Transaction Name", new Date(119, 8, 22, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00008", "Transaction Name", new Date(119, 8, 25, 11, 23, 30), new BigDecimal("-12.5")));
//        transactionList.add(new Transaction("00009", "Transaction Name", new Date(119, 9, 2, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00010", "Transaction Name", new Date(119, 9, 22, 11, 23, 30), new BigDecimal("-12.5")));
//        transactionList.add(new Transaction("00011", "Transaction Name", new Date(119, 9, 23, 11, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00012", "Transaction Name", new Date(119, 9, 23, 13, 23, 30), new BigDecimal("12.5")));
//        transactionList.add(new Transaction("00013", "Transaction Name", new Date(119, 9, 23, 115, 23, 30), new BigDecimal("12.5")));

        listener.onComplete(transactionList, null);
    }
}
