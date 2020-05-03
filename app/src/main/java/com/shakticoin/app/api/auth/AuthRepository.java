package com.shakticoin.app.api.auth;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.util.Debug;

import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository extends BackendRepository {
    public LoginService loginService;

    public AuthRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginService = retrofit.create(LoginService.class);
    }

    /**
     * Login to the backend and return user's information if successful.
     */
    public void login(@NonNull String username, @NonNull String password, @NonNull OnCompleteListener<Void> listener) {
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        Call<TokenResponse> call = loginService.token(credentials);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    TokenResponse resp = response.body();
                    if (resp != null) {
                        Session.setAccessToken(resp.getAccess());
                        Session.setRefreshToken(resp.getRefresh());
                        listener.onComplete(null,null);
                    }
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
        TokenParameters parameters = new TokenParameters(null, refreshToken);
        loginService.refresh(parameters).enqueue(new Callback<TokenResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    TokenResponse resp = response.body();
                    if (resp != null) {
                        Session.setAccessToken(resp.getAccess());
                        if (resp.getRefresh() != null) {
                            Session.setRefreshToken(resp.getRefresh());
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
