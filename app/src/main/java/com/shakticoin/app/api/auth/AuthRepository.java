package com.shakticoin.app.api.auth;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.UnsafeHttpClient;
import com.shakticoin.app.util.Debug;

import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository extends BackendRepository {
    public LoginService loginService;
    private static String IAM_STG = "Basic OWIwOWNiZTgtYzg4My00YmU1LWFhZjgtNjlkYTRiYjNlOTQyOmVJWWc5Mlk4cyZGUg==";
    private static String IAM_QA = "Basic Mzg5ZmQ3ODMtOTUzMy00YTliLTgyMTUtODFjMWQ5YmFhMDdkOmk1S0tTcFBDRzFsa3BCREdNTk92Z2E0RE9vdVdyOHhVY1l1cnRHWU0=";

    public AuthRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.IAM_BASE_URL)
                .client(UnsafeHttpClient.getUnsafeOkHttpClient())   // FIXME: this must be removed some day
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginService = retrofit.create(LoginService.class);
    }

    /**
     * Login to the backend and return user's information if successful.
     */
    public void login(@NonNull String username, @NonNull String password, boolean rememberMe, @NonNull OnCompleteListener<TokenResponse> listener) {
        Call<TokenResponse> call = loginService.token(IAM_STG, "password", "openid+profile", username, password);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    TokenResponse resp = response.body();
                    if (resp != null) {
                        Session.setAccessToken(resp.getAccess_token());
                        if (resp.getRefresh_token() != null) {
                            Session.setRefreshToken(resp.getRefresh_token(), rememberMe);
                        }
                    }
                    listener.onComplete(resp, null);
                } else {
                    returnError(listener, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void refreshToken(@NonNull String refreshToken, @NonNull OnCompleteListener<TokenResponse> listener) {
        loginService.refresh(IAM_STG, "refresh_token", refreshToken).enqueue(new Callback<TokenResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    TokenResponse resp = response.body();
                    if (resp != null) {
                        Session.setAccessToken(resp.getAccess_token());
                        if (resp.getRefresh_token() != null) {
                            Session.setRefreshToken(resp.getRefresh_token());
                        }
                        listener.onComplete(resp, null);
                        return;
                    }
                    listener.onComplete(resp, null);
                } else {
                    Debug.logErrorResponse(response);
                    Session.clean();
                    listener.onComplete(null, new UnauthorizedException());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
