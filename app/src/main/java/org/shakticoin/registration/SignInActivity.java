package org.shakticoin.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.Session;
import org.shakticoin.api.auth.Credentials;
import org.shakticoin.api.auth.LoginService;
import org.shakticoin.api.auth.LoginServiceResponse;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.MinerService;
import org.shakticoin.util.Debug;
import org.shakticoin.util.PreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignInActivity extends AppCompatActivity {
    private EditText ctrlUsername;
    private EditText ctrlPassword;
    private ProgressBar progressBar;

    private LoginService loginService;
    private MinerService minerService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        progressBar = findViewById(R.id.progressBar);
        ctrlUsername = findViewById(R.id.username);
        ctrlPassword = findViewById(R.id.password);
        ctrlPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onLogin(v);
                return true;
            }
            return false;
        });
        TextView ctrlRegisterNowLink = findViewById(R.id.register_now_link);
        final Context self = this;
        ctrlRegisterNowLink.setOnClickListener(v -> {
            Intent intent = new Intent(self, SignUpActivity.class);
            startActivity(intent);
        });
        TextView ctrlForgotPwd = findViewById(R.id.forgotPassword);
        ctrlForgotPwd.setOnClickListener(v -> {
            Intent intent = new Intent(self, RecoveryPasswordActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
        minerService = retrofit.create(MinerService.class);

    }

    public void onLogin(View view) {

        String username = ctrlUsername.getText().toString();
        String password = ctrlPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.login_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        final Activity self = this;
        Call<LoginServiceResponse> call = loginService.login(credentials);
        call.enqueue(new Callback<LoginServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginServiceResponse> call, @NonNull Response<LoginServiceResponse> response) {
                if (call.isExecuted() && response.isSuccessful()) {
                    LoginServiceResponse resp = response.body();
                    if (resp != null) {
                        Session.key(resp.getKey());
                        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                        prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, true).apply();

                        Call<MinerDataResponse> call1 = minerService.getUserInfo(Session.getAuthorizationHeader());
                        call1.enqueue(new Callback<MinerDataResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<MinerDataResponse> call, @NonNull Response<MinerDataResponse> response) {
                                if (call.isExecuted() && response.isSuccessful()) {
                                    MinerDataResponse body = response.body();
                                    if (body != null) {
                                        Session.setUser(body.getUser());
                                    }
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onFailure(@NonNull Call<MinerDataResponse> call, @NonNull Throwable t) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(self, R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                                Debug.logException(t);
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginServiceResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(self, R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                Debug.logException(t);
            }
        });
    }
}
