package com.shakticoin.app.api.wallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
                .baseUrl(BaseUrl.WALLETSERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WalletService.class);
        authRepository = new AuthRepository();
    }

    /**
     * Before a user enters the wallet page we must have this wallet. The wallet bytes (that is
     * a wallet ID) must be stored in encrypted preferences (for now, better to get it in user's
     * information).
     * @param user User information does not contains wallet but it should. Included for the future.
     * @return
     */
    public String getExistingWallet(@Nullable User user) {
        String walletBytes = null;
        try {
            Context context = ShaktiApplication.getContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KeyGenParameterSpec keyGenParams = MasterKeys.AES256_GCM_SPEC;
                String masterKeyAlias = MasterKeys.getOrCreate(keyGenParams);
                SharedPreferences prefs = EncryptedSharedPreferences.create(
                        "ss", masterKeyAlias, context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                walletBytes = prefs.getString(PreferenceHelper.PREF_WALLET_BYTES, null);
            } else {
                SharedPreferences prefs =
                        context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                walletBytes = prefs.getString(PreferenceHelper.PREF_WALLET_BYTES, null);
            }

        } catch (IOException | GeneralSecurityException e) {
            Debug.logException(e);
        }
        return walletBytes;
    }

    /**
     * Store wallet ID locally for future use but we must save it in the user profile when API
     * will allow this.
     * @param walletBytes A wallet ID
     */
    public void storeWallet(@NonNull String walletBytes) {
        if (TextUtils.isEmpty(walletBytes)) throw new IllegalArgumentException();
        try {
            Context context = ShaktiApplication.getContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // store refresh token in an encrypted storage in order to login automatically
                KeyGenParameterSpec keyGenParams = MasterKeys.AES256_GCM_SPEC;
                String masterKeyAlias = MasterKeys.getOrCreate(keyGenParams);
                SharedPreferences prefs = EncryptedSharedPreferences.create(
                        "ss", masterKeyAlias, context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PreferenceHelper.PREF_WALLET_BYTES, walletBytes).apply();
            } else {
                SharedPreferences prefs =
                        context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                prefs.edit().putString(PreferenceHelper.PREF_WALLET_BYTES, walletBytes).apply();
            }

        } catch (IOException | GeneralSecurityException e) {
            Debug.logException(e);
        }
    }

    private void getWalletSession(@NonNull OnCompleteListener<Long> listener) {
        SessionModelRequest parameters = new SessionModelRequest();
        parameters.setWalletBytes(getExistingWallet(null));
        service.getSession(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<SessionModelResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<SessionModelResponse> call, Response<SessionModelResponse> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    SessionModelResponse result = response.body();
                    if (result != null) {
                        String message = result.getMessage();
                        if (message != null) Debug.logDebug(message);
                        listener.onComplete(result.getSessionToken(), null);
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
                                getWalletSession(listener);
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
            public void onFailure(Call<SessionModelResponse> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    /**
     * Creates a new wallet and return wallet bytes as a string. Wallet bytes must be stored and
     * used in parameters of other calls.
     * @param passphrase The wallet password. Basically, it's encryption passphrase that is not
     *                   depends on user's account password.
     */
    public void createWallet(@Nullable String passphrase, @NonNull OnCompleteListener<String> listener) {
        WalletModelRequest parameters = new WalletModelRequest(null, passphrase);
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

    public void getAddress(@NonNull OnCompleteListener<String> listener) {
        Long sessionToken = Session.getWalletSessionToken();
        if (sessionToken == null) {
            getWalletSession(new OnCompleteListener<Long>() {
                @Override
                public void onComplete(Long sessionToken, Throwable error) {
                    if (error != null) {
                        listener.onComplete(null, error);
                        return;
                    }
                    Session.setWalletSessionToken(sessionToken);
                    getAddress(listener);
                }
            });
            return;
        }
        WalletAddressModelRequest parameters = new WalletAddressModelRequest();
        parameters.setSessionToken(sessionToken);
        service.getWalletAddress(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<WalletAddressModelResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<WalletAddressModelResponse> call, Response<WalletAddressModelResponse> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null);
                if (response.isSuccessful()) {
                    WalletAddressModelResponse results = response.body();
                    if (results != null) {
                        String message = results.getMessage();
                        if (message != null) Debug.logDebug(message);
                        String walletAddress = results.getWalletAddress();
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
                                getAddress(listener);
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
            public void onFailure(Call<WalletAddressModelResponse> call, Throwable t) {
                Session.setWalletSessionToken(null);
                returnError(listener, t);
            }
        });
    }

    public void getBalance(@NonNull OnCompleteListener<BigDecimal> listener) {
        Long sessionToken = Session.getWalletSessionToken();
        if (sessionToken == null) {
            getWalletSession(new OnCompleteListener<Long>() {
                @Override
                public void onComplete(Long sessionToken, Throwable error) {
                    if (error != null) {
                        listener.onComplete(null, error);
                        return;
                    }
                    Session.setWalletSessionToken(sessionToken);
                    getBalance(listener);
                }
            });
            return;
        }
        WalletBalanceModelRequest parameters = new WalletBalanceModelRequest();
        parameters.setSessionToken(sessionToken);
        service.getWalletBalance(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<WalletBalanceModelResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<WalletBalanceModelResponse> call, Response<WalletBalanceModelResponse> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null); // clear session token
                if (response.isSuccessful()) {
                    WalletBalanceModelResponse results = response.body();
                    if (results != null) {
                        String message = results.getMessage();
                        if (message != null) Debug.logDebug(message);
                        String walletBalance = (String) results.getWalletBalance();
                        BigDecimal balance = BigDecimal.ZERO;
                        if (!TextUtils.isEmpty(walletBalance) && TextUtils.isDigitsOnly(walletBalance)) {
                            balance = new BigDecimal(walletBalance);
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
                                getBalance(listener);
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
            public void onFailure(Call<WalletBalanceModelResponse> call, Throwable t) {
                Session.setWalletSessionToken(null);
                returnError(listener, t);
            }
        });
    }

    public void transfer(@NonNull String address, @NonNull Long amount, @Nullable String message, @NonNull OnCompleteListener<TransferModelResponse> listener) {
        Long sessionToken = Session.getWalletSessionToken();
        if (sessionToken == null) {
            getWalletSession(new OnCompleteListener<Long>() {
                @Override
                public void onComplete(Long token, Throwable error) {
                    if (error != null) {
                        listener.onComplete(null, error);
                        return;
                    }
                    Session.setWalletSessionToken(token);
                    transfer(address, amount, message, listener);
                }
            });
            return;
        }
        CoinModel parameters = new CoinModel();
        parameters.setToAddress(address);
        parameters.setValueInToshi(amount.toString());
        parameters.setSessionToken(sessionToken);
        parameters.setMessageForRecipient(message != null ? message
                : ShaktiApplication.getContext().getString(R.string.dlg_sxe_default_message));
        service.transferSxeCoins(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<TransferModelResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<TransferModelResponse> call, Response<TransferModelResponse> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null);
                if (response.isSuccessful()) {
                    TransferModelResponse results = response.body();
                    if (results != null) {
                        String message = results.getMessage();
                        if (message != null) Debug.logDebug(message);
                        listener.onComplete(results, null);
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
                                transfer(address, amount, message, listener);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        listener.onComplete(null,
                                new RemoteException(
                                        ShaktiApplication.getContext().getString(R.string.dlg_sxe_err_transfer),
                                        response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<TransferModelResponse> call, Throwable t) {
                Session.setWalletSessionToken(null);
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
