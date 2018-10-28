package org.shakticoin.api.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.User;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Debug;
import org.shakticoin.util.PreferenceHelper;

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
                    if (response.isSuccessful()) {
                        LoginServiceResponse resp = response.body();
                        if (resp != null) {
                            Session.key(resp.getKey());
                            listener.onComplete(null,null);
                        }
                    } else {
                        Debug.logDebug(response.toString());
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
}
