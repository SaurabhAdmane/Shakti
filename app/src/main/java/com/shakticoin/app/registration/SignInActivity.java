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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.api.user.UserAccount;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivitySigninBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.WalletActivity;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private ActivitySigninBinding binding;

    private AuthRepository authRepository = new AuthRepository();
    private UserRepository userRepository = new UserRepository();

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

        // Enable "Remember Me" in debug mode only
        if (BuildConfig.DEBUG) {
            final Activity self = this;
            binding.rememberMe.setVisibility(View.VISIBLE);
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
        }
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

        final Activity self = this;
        authRepository.login(username, password, new OnCompleteListener<TokenResponse>() {
            @Override
            public void onComplete(TokenResponse tokens, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(self, R.string.err_login_failed, Toast.LENGTH_LONG).show();
                    return;
                }
                Session.setAccessToken(tokens.getAccess_token());
                Session.setRefreshToken(tokens.getRefresh_token(), rememberMe);
                SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, true).apply();

                binding.progressBar.setVisibility(View.VISIBLE);
                userRepository.getUserAccount(new OnCompleteListener<UserAccount>() {
                    @Override
                    public void onComplete(UserAccount value, Throwable error) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) {
                            if (error instanceof UnauthorizedException) {
                                startActivity(Session.unauthorizedIntent(self));
                            } else {
                                Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                                Debug.logException(error);
                            }
                            return;
                        }

                        // go to the wallet
                        startActivity(new Intent(self, WalletActivity.class));
                    }
                });
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
