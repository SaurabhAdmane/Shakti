package com.shakticoin.app.registration;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.auth.Credentials;
import com.shakticoin.app.api.auth.LoginService;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivitySigninBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.WalletActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    private ActivitySigninBinding binding;

    private LoginService loginService;
    private UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin);
        binding.setLifecycleOwner(this);

        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onLogin(v);
                return true;
            }
            return false;
        });

        binding.usernameLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.passwordLayout.setValidator((view, value) -> {
            // password must be longer than 8 chars
            return value != null && value.length() > Validator.MIN_PASSWD_LEN;
        });

        final Activity self = this;
        binding.rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean deviceSecure = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                deviceSecure = keyguardManager != null && keyguardManager.isDeviceSecure();
            }
            if (buttonView.isChecked() && !deviceSecure) {
                buttonView.setChecked(false);
                Toast.makeText(self, R.string.err_device_not_secure, Toast.LENGTH_LONG).show();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
        userRepository = new UserRepository();

    }

    public void onLogin(View view) {

        String username = Objects.requireNonNull(binding.username.getText()).toString();
        boolean validationSuccessful = true;
        if (!Validator.isEmail(username)) {
            validationSuccessful = false;
            binding.usernameLayout.setError(getString(R.string.err_loging_must_be_email));
        }
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        if (TextUtils.isEmpty(password) || password.length() < Validator.MIN_PASSWD_LEN) {
            validationSuccessful = false;
            binding.passwordLayout.setError(getString(R.string.err_password_invalid, Validator.MIN_PASSWD_LEN));
        }
        if (!validationSuccessful) {
            return;
        }

        boolean rememberMe = binding.rememberMe.isChecked();

        binding.progressBar.setVisibility(View.VISIBLE);

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        final Activity self = this;
        Call<TokenResponse> call = loginService.token(credentials);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        TokenResponse resp = response.body();
                        if (resp != null) {
                            Session.setAccessToken(resp.getAccess());
                            Session.setRefreshToken(resp.getRefresh(), rememberMe, self);
                            SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                            prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, true).apply();

                            userRepository.getUserInfo(new OnCompleteListener<User>() {
                                @Override
                                public void onComplete(User value, Throwable error) {
                                    binding.progressBar.setVisibility(View.INVISIBLE);

                                    if (error != null) {
                                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                                        Debug.logException(error);
                                        return;
                                    }

                                    // go to the wallet
                                    startActivity(new Intent(self, WalletActivity.class));
                                }
                            });

                        }
                    } else {
                        Debug.logErrorResponse(response);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(self, R.string.err_login_failed, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(self, Debug.getFailureMsg(self, t), Toast.LENGTH_SHORT).show();
                Debug.logException(t);
            }
        });
    }

    /**
     * We cannot continue before email is confirmed and just show information window to the user
     */
    private void showNotConfirmed() {
        DialogConfirmEmail dialog = DialogConfirmEmail.getInstance(true);
        dialog.show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
    }

    public void onRecoveryPassword(View view) {
        String emailAddress = Objects.requireNonNull(binding.username.getText()).toString();
        Intent intent = new Intent(this, RecoveryPasswordActivity.class);
        if (Validator.isEmail(emailAddress))
            intent.putExtra(CommonUtil.prefixed("emailAddress", this), emailAddress);
        startActivity(intent);
    }

    public void onRegisterNow(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
