package com.shakticoin.app.api.selfId;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

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
import com.shakticoin.app.api.kyc.KYCService;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.api.kyc.KycDocType;
import com.shakticoin.app.api.kyc.KycUserModel;
import com.shakticoin.app.api.kyc.KycUserView;
import com.shakticoin.app.api.license.CheckoutResponse;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class SelfRepository extends BackendRepository {
    private KYCService service;
    private AuthRepository authRepository;

    public SelfRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.SELF_ID_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(KYCService.class);
        authRepository = new AuthRepository();
    }

    public void getWalletRequestAPI(String walletBytes, OnCompleteListener<String> listener) {
        getWalletRequestAPI(walletBytes, listener, false);
    }
    public void getWalletRequestAPI(String walletBytes, OnCompleteListener<String> listener, boolean hasRecover401) {
        service.getWalletByte(Session.getAuthorizationHeader(), walletBytes).enqueue(new Callback<KycUserView>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<KycUserView> call, Response<KycUserView> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    KycUserView body = response.body();
                    if (body != null) {
                        try {
                            String content = body.getWalletBytes();
                            listener.onComplete(content, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    switch (response.code()) {
                        case 401:
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
                                return;
                            }
                            break;
                        default:
                            Debug.logErrorResponse(response);
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<KycUserView> call, Throwable t) {
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
