package com.shakticoin.app.api.bizvault;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
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
import com.shakticoin.app.api.onboard.ResponseBean;
import com.shakticoin.app.api.onboard.ResponseVault;
import com.shakticoin.app.api.wallet.CoinModel;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.SessionModelRequest;
import com.shakticoin.app.api.wallet.SessionModelResponse;
import com.shakticoin.app.api.wallet.Transaction;
import com.shakticoin.app.api.wallet.TransferModelResponse;
import com.shakticoin.app.api.wallet.WalletAddressModelRequest;
import com.shakticoin.app.api.wallet.WalletAddressModelResponse;
import com.shakticoin.app.api.wallet.WalletBalanceModelRequest;
import com.shakticoin.app.api.wallet.WalletBalanceModelResponse;
import com.shakticoin.app.api.wallet.WalletModelRequest;
import com.shakticoin.app.api.wallet.WalletService;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class BizvaultRepository extends BackendRepository {
    private BizvalutService service;
    private AuthRepository authRepository;

    public BizvaultRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BIZVAULT_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BizvalutService.class);
        authRepository = new AuthRepository();
    }

//    https://bizvault-stg.shakticoin.com/bizvault/api/v1/bizvaults/verify/bizvaultid
    public void bizvaultStatus(@NonNull OnCompleteListener<Boolean> listener) {
        bizvaultStatus(listener, false);
    }
    public void bizvaultStatus(@NonNull OnCompleteListener<Boolean> listener, boolean hasRecover401) {

        service.bizvaultStatus(Session.getAuthorizationHeader()).enqueue(new Callback<ResponseVault>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ResponseVault> call, Response<ResponseVault> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null);
                if (response.isSuccessful()) {
                    ResponseVault results = response.body();
                    if (results != null) {
                        String message = results.getMessage();
                        if (message != null) Debug.logDebug(message);
                        listener.onComplete(results.getDetails().getBizvaultIdStatus(), null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete(null, new UnauthorizedException());
                                        return;
                                    }
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
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
            public void onFailure(Call<ResponseVault> call, Throwable t) {
                Session.setWalletSessionToken(null);
                returnError(listener, t);
            }
        });
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        authRepository.setLifecycleOwner(lifecycleOwner);
    }



}
