package com.shakticoin.app.api.referrals;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.RemoteMessageException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.api.bizvault.BizvalutService;
import com.shakticoin.app.api.onboard.ResponseBean;
import com.shakticoin.app.api.referral.ReferralParameters;
import com.shakticoin.app.api.referral.model.Referral;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
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

//    [9:13 PM, 10/22/2020] if bonusBounty Status = true
//          var bonusId: String
//    var shaktiID: String
//    var walletType: String
//    var walletOwnerAnniversary: String
//    var promisedGenisisBounty: Int
//    var lockedGenesisBounty: Int
//    var remainingMonths: Int
//    var successfulReferralCount: Int
//    var offerId: String

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
                        listener.onComplete(results.getLockedGenesisBounty(), null);
                    } else listener.onComplete(null, new IllegalStateException());
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete("-1", new UnauthorizedException());
                                        return;
                                    }
                                }
                            });
                        } else {
                            listener.onComplete("-1", new UnauthorizedException());
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        listener.onComplete("-1", new UnauthorizedException());
                        return;
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


    public void getReferral(@NonNull ReferralParameters parameters, @NonNull OnCompleteListener<Referral> listener) {
        getReferral(parameters, listener, false);
    }
    public void getReferral(@NonNull ReferralParameters parameters, @NonNull OnCompleteListener<Referral> listener, boolean hasRecover401) {

        service.getReferral(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Referral>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Referral> call, Response<Referral> response) {
                Debug.logDebug(response.toString());
                Session.setWalletSessionToken(null);
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
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
                        case 400:
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                                try {
                                    RemoteMessageException e = new RemoteMessageException(response.message(), response.code());
                                    JSONObject errorResponse = new JSONObject(errorBody.string());
                                    Iterator<String> keys = errorResponse.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONArray errorMessageList = errorResponse.getJSONArray(key);
                                        String errorMessage = (String) errorMessageList.get(0);
                                        e.addValidationError(key, errorMessage);
                                    }
                                    listener.onComplete(null, e);
                                } catch (IOException | JSONException e) {
                                    listener.onComplete(null, e);
                                }
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
            public void onFailure(Call<Referral> call, Throwable t) {
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
