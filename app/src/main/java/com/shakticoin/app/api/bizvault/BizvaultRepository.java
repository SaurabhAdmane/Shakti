package com.shakticoin.app.api.bizvault;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.shakticoin.app.BuildConfig;
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
import com.shakticoin.app.api.onboard.ResponseVault;
import com.shakticoin.app.util.Debug;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class BizvaultRepository extends BackendRepository {
    private final BizvalutService service;
    private final AuthRepository authRepository;
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    public BizvaultRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(BuildConfig.DEBUG ? httpLoggingInterceptor.setLevel(
                        HttpLoggingInterceptor.Level.BODY
                        ) : httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BIZVAULT_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
