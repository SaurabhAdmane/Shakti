package com.shakticoin.app.api.kyc;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class KYCRepository extends BackendRepository {
    private KYCService service;
    private AuthRepository authRepository;

    public KYCRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.KYC_USER_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(KYCService.class);
        authRepository = new AuthRepository();
    }

    public void getUserDetails(OnCompleteListener<Map<String, Object>> listener) {
        service.getUserDetails(Session.getAuthorizationHeader()).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, Object> values = response.body();
                    listener.onComplete(values, null);
                } else {
                    switch (response.code()) {
                        case 401:
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete(null, new UnauthorizedException());
                                        return;
                                    }
                                    getUserDetails(listener);
                                }
                            });
                            break;
                        case 404:
                            ResponseBody errorBody = response.errorBody();
                            String message = response.message();
                            if (errorBody != null) {
                                try {
                                    String errorResponse = errorBody.string();
                                    JSONObject json = new JSONObject(errorResponse);
                                    if (json.has("message")) {
                                        message = json.getString("message");
                                    }
                                } catch (IOException | JSONException e) {
                                    Debug.logDebug(e.getMessage());
                                }
                            }
                            listener.onComplete(null, new RemoteException(message, 404));
                            break;
                        default:
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

    public void createUserDetails(@NonNull KycUserModel parameters, @NonNull OnCompleteListener<Map<String, Object>> listener) {
        service.createUserDetails(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    JSONObject jsonResponse = errorBodyJSON(response.errorBody());
                    if (jsonResponse != null && jsonResponse.has("rootArray")) {
                        try {
                            JSONArray msgArray = jsonResponse.getJSONArray("rootArray");
                            listener.onComplete(null, new RemoteException(msgArray.getString(0)));
                            return;
                        } catch (JSONException e) {
                            Debug.logDebug(e.getMessage());
                        }
                    }
                    returnError(listener, response);
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void updateUserDetails(KycUserModel parameters, OnCompleteListener<Map<String, Object>> listener) {
        service.updateUserDetails(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    JSONObject jsonResponse = errorBodyJSON(response.errorBody());
                    if (jsonResponse != null && jsonResponse.has("rootArray")) {
                        try {
                            JSONArray msgArray = jsonResponse.getJSONArray("rootArray");
                            listener.onComplete(null, new RemoteException(msgArray.getString(0)));
                            return;
                        } catch (JSONException e) {
                            Debug.logDebug(e.getMessage());
                        }
                    }
                    returnError(listener, response);
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    /**
     * Retrieve KYC information.
     * @deprecated
     */
    public void getKycCategories(OnCompleteListener<List<KycCategory>> listener) {
        service.getKycCategories(Session.getAuthorizationHeader()).enqueue(new Callback<List<KycCategory>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<KycCategory>> call, Response<List<KycCategory>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<KycCategory> categories = response.body();
                    if (categories != null && categories.size() > 0) {
                        Collections.sort(categories,
                                (o1, o2) -> o1.getOrder_no().compareTo(o2.getOrder_no()));
                    }
                    listener.onComplete(categories, null);
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getKycCategories(listener);
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
            public void onFailure(Call<List<KycCategory>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
