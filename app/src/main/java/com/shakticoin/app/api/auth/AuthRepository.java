package com.shakticoin.app.api.auth;

import android.content.Context;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.util.Debug;

import org.json.JSONException;
import org.json.JSONObject;
import com.shakticoin.app.R;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;

import java.io.IOException;

import okhttp3.ResponseBody;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
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
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException(response.message(), response.code()));
                    } else {
                        Debug.logErrorResponse(response);
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    public void refreshToken(@NonNull String refreshToken, @NonNull OnCompleteListener<TokenResponse> listener) {
        TokenParameters parameters = new TokenParameters(null, refreshToken);
        loginService.refresh(parameters).enqueue(new Callback<TokenResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse resp = response.body();
                    listener.onComplete(resp, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        Debug.logErrorResponse(response);
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    /**
     * Call backend to refresh token in synchronous mode.
     * @return Returns true if the tokens are updated
     */
    public boolean refreshTokenSync() {
        TokenParameters parameters = new TokenParameters();
        parameters.setRefresh(Session.getRefreshToken());
        try {
            Response<TokenResponse> response = loginService.refresh(parameters).execute();
            if (response.isSuccessful()) {
                TokenResponse tokenResponse = response.body();
                if (tokenResponse != null) {
                    Session.setAccessToken(tokenResponse.getAccess());
                    Session.setRefreshToken(tokenResponse.getRefresh());
                    return true;
                }
            }

        } catch (IOException e) {
            Debug.logException(e);
        }
        return false;
    }

//    public void checkEmailPhoneExists(Context context, String emailAddress, String phoneNumber, @NonNull OnCompleteListener<Boolean> listener) {
//        if (emailAddress == null && phoneNumber == null) {
//            listener.onComplete(null, new IllegalArgumentException("Both phone number and email cannot be empty."));
//        }
//
//        Call<ResponseBody> call = loginService.checkEmailPhone(
//                new CheckEmailPhoneParams(emailAddress, phoneNumber));
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (call.isExecuted()) {
//                    Debug.logDebug(response.toString());
//                    if (response.isSuccessful()) {
//                        listener.onComplete(Boolean.TRUE, null);
//                    } else {
//                        String errMsg = null;
//                        try {
//                            ResponseBody responseBody = response.errorBody();
//                            if (responseBody != null) {
//                                JSONObject json = new JSONObject(responseBody.string());
//                                if (json.has("email") && json.has("phone_number")) {
//                                    errMsg = context.getString(R.string.err_email_phone_exists);
//                                } else if (json.has("email")) {
//                                    errMsg = context.getString(R.string.err_email_exists);
//                                } else if (json.has("phone_number")) {
//                                    errMsg = context.getString(R.string.err_phone_exists);
//                                }
//                            }
//
//                        } catch (IOException | JSONException e) {
//                            Debug.logException(e);
//                        }
//
//                        if (errMsg == null) errMsg = context.getString(R.string.err_unexpected);
//                        listener.onComplete(Boolean.FALSE, new RemoteException(errMsg));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Debug.logDebug(t.getMessage());
//                listener.onComplete(Boolean.FALSE, t);
//            }
//        });
//
//    }
}
