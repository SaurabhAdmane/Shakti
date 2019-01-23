package org.shakticoin.api.auth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.User;
import org.shakticoin.registration.SignUpAddressFragment;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Debug;
import org.shakticoin.util.PreferenceHelper;

import java.io.IOException;

import okhttp3.ResponseBody;
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
    public void login(@NonNull String username, @NonNull String password, @NonNull OnCompleteListener listener) {
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setEmail(username);
        credentials.setPassword(password);

        Call<LoginServiceResponse> call = loginService.login(credentials);
        call.enqueue(new Callback<LoginServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginServiceResponse> call, @NonNull Response<LoginServiceResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        LoginServiceResponse resp = response.body();
                        if (resp != null) {
                            Session.key(resp.getKey());
                            listener.onComplete(null,null);
                        }
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginServiceResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    public void checkEmailPhoneExists(Context context, String emailAddress, String phoneNumber, @NonNull OnCompleteListener<Boolean> listener) {
        if (emailAddress == null && phoneNumber == null) {
            listener.onComplete(null, new IllegalArgumentException("Both phone number and email cannot be empty."));
        }

        Call<ResponseBody> call = loginService.checkEmailPhone(
                new CheckEmailPhoneParams(emailAddress, phoneNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (call.isExecuted()) {
                    if (response.isSuccessful()) {
                        listener.onComplete(Boolean.TRUE, null);
                    } else {
                        String errMsg = null;
                        try {
                            ResponseBody responseBody = response.errorBody();
                            if (responseBody != null) {
                                JSONObject json = new JSONObject(responseBody.string());
                                if (json.has("email") && json.has("phone_number")) {
                                    errMsg = context.getString(R.string.err_email_phone_exists);
                                } else if (json.has("email")) {
                                    errMsg = context.getString(R.string.err_email_exists);
                                } else if (json.has("phone_number")) {
                                    errMsg = context.getString(R.string.err_phone_exists);
                                }
                            }

                        } catch (IOException | JSONException e) {
                            Debug.logException(e);
                        }

                        if (errMsg == null) errMsg = context.getString(R.string.err_unexpected);
                        listener.onComplete(Boolean.FALSE, new RemoteException(errMsg));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Debug.logDebug(t.getMessage());
                listener.onComplete(Boolean.FALSE, t);
            }
        });

    }
}
