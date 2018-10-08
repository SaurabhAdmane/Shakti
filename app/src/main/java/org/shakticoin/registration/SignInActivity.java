package org.shakticoin.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.Session;
import org.shakticoin.api.auth.Credentials;
import org.shakticoin.api.auth.LoginService;
import org.shakticoin.api.auth.LoginServiceResponse;
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

    private LoginService loginService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ctrlUsername = findViewById(R.id.username);
        ctrlPassword = findViewById(R.id.password);
        ctrlPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLogin(v);
                    return true;
                }
                return false;
            }
        });
        TextView ctrlRegisterNowLink = findViewById(R.id.register_now_link);
        final Context self = this;
        ctrlRegisterNowLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, SignUpActivity.class);
                startActivity(intent);
            }
        });
        TextView ctrlForgotPwd = findViewById(R.id.forgotPassword);
        ctrlForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, RecoveryPasswordActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);

    }

    public void onLogin(View view) {

        String username = ctrlUsername.getText().toString();
        String password = ctrlPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.login_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

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
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginServiceResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
            }
        });
    }
}
