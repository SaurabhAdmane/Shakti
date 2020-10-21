package com.shakticoin.app.api.referrals;

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
import com.shakticoin.app.api.bizvault.BizvalutService;
import com.shakticoin.app.api.onboard.ResponseBean;
import com.shakticoin.app.util.Debug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class BountyRepository extends BackendRepository {
    private ReferralService service;
    private AuthRepository authRepository;

    public BountyRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.REFERRAL_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ReferralService.class);
        authRepository = new AuthRepository();
    }

    public void bountyStatus(@NonNull OnCompleteListener<String> listener) {
        bountyStatus(listener, false);
    }
    public void bountyStatus(@NonNull OnCompleteListener<String> listener, boolean hasRecover401) {

        service.bountyStatus(Session.getAuthorizationHeader()).enqueue(new Callback<ResponseBean>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null);
                if (response.isSuccessful()) {
                    ResponseBean results = response.body();
                    if (results != null) {
                        String message = results.getMessage();
                        if (message != null) Debug.logDebug(message);
                        listener.onComplete(results.getSessionToken(), null);
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
//                        listener.onComplete(null,
//                                new RemoteException(
//                                        ShaktiApplication.getContext().getString(R.string.not_found),
//                                        response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
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
