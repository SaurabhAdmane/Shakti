package com.shakticoin.app.api.referral;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteMessageException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.api.referral.model.EffortRate;
import com.shakticoin.app.api.referral.model.Referral;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_EMAIL;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_FACEBOOK;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_INSTAGRAM;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_LINKEDIN;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_PINTEREST;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_PLUS;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_SKYPE;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_TUMBLR;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_TWITTER;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_UNKNOWN;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_VK;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_WECHAT;

public class ReferralRepository extends BackendRepository {
    private ReferralService service;
    private AuthRepository authRepository;

    public ReferralRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ReferralService.class);
        authRepository = new AuthRepository();

    }

    /**
     * Returns summary of the effort rate data.
     */
    public void getEffortRates(@NonNull OnCompleteListener<List<EffortRate>> listener) {
        // TODO: return real data when API is ready

        // mockup data
        ArrayList<EffortRate> resultList = new ArrayList<>();
        resultList.add(new EffortRate(LEAD_SOURCE_FACEBOOK, 64, 20, 16));
        resultList.add(new EffortRate(LEAD_SOURCE_INSTAGRAM, 34, 29, 37));
        resultList.add(new EffortRate(LEAD_SOURCE_PLUS, 34, 29, 37));
        resultList.add(new EffortRate(LEAD_SOURCE_LINKEDIN, 20, 34, 46));
        resultList.add(new EffortRate(LEAD_SOURCE_TWITTER, 0, 42, 58));
        resultList.add(new EffortRate(LEAD_SOURCE_PINTEREST, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_SKYPE, 20, 20, 20)); // not 100, error for testing
        resultList.add(new EffortRate(LEAD_SOURCE_VK, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_WECHAT, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_TUMBLR, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_EMAIL, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_UNKNOWN, 3, 97, 0));

        listener.onComplete(resultList, null);
    }


    /**
     * Add new referral.
     */
    public void addReferral(@NonNull ReferralParameters parameters, @NonNull OnCompleteListener<Referral> listener) {
        addReferral(parameters, listener, false);
    }
    public void addReferral(@NonNull ReferralParameters parameters, @NonNull OnCompleteListener<Referral> listener, boolean hasRecover401) {
        service.addReferral(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Referral>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Referral> call, Response<Referral> response) {
                Debug.logDebug(response.toString());
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
                                        addReferral(parameters, listener, true);
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
                returnError(listener, t);
            }
        });
    }

    /**
     * Return personal data for user's referrals.
     */
    public void getReferrals(String status, @NonNull OnCompleteListener<List<Referral>> listener) {
        getReferrals(status, listener, false);
    }
    public void getReferrals(String status, @NonNull OnCompleteListener<List<Referral>> listener, boolean hasRecover401) {
        service.getReferrals(Session.getAuthorizationHeader()).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<Referral> results = new ArrayList<>();
                    Map<String, Object> resp = response.body();
                    List<Map<String, Object>> referrals = (List<Map<String, Object>>) resp.get("referrals");
                    if (referrals != null && referrals.size() > 0) {
                        for (Map<String, Object> r : referrals) {
                            // filter by status
                            if (status != null && !status.equals(r.get("status"))) continue;
                            results.add(new Referral(r));
                        }
                    }
                    listener.onComplete(results, null);
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
                                    getReferrals(status, listener, true);
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
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

    public void getReferralsByReferrer(String id, @NonNull OnCompleteListener<List<Referral>> listener) {
        getReferralsByReferrer(id, listener, false);
    }
    public void getReferralsByReferrer(String id, @NonNull OnCompleteListener<List<Referral>> listener, boolean hasRecover401) {
        service.findReferralByReferrer(Session.getAuthorizationHeader(), id).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<Referral> results = new ArrayList<>();
                    Map<String, Object> resp = response.body();
                    List<Map<String, Object>> referrals = (List<Map<String, Object>>) resp.get("referrals");
                    if (referrals != null && referrals.size() > 0) {
                        for (Map<String, Object> r : referrals) {
                            results.add(new Referral(r));
                        }
                    }
                    listener.onComplete(results, null);
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
                                    getReferralsByReferrer(id, listener, true);
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
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

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        authRepository.setLifecycleOwner(lifecycleOwner);
    }
}
