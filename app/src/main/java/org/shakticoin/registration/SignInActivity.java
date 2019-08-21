package org.shakticoin.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.Session;
import org.shakticoin.api.auth.Credentials;
import org.shakticoin.api.auth.LoginService;
import org.shakticoin.api.auth.LoginServiceResponse;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.MinerRepository;
import org.shakticoin.databinding.ActivitySigninBinding;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Debug;
import org.shakticoin.util.PreferenceHelper;
import org.shakticoin.util.Validator;
import org.shakticoin.wallet.WalletActivity;
import org.shakticoin.widget.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignInActivity extends AppCompatActivity {
    private ActivitySigninBinding binding;

    private LoginService loginService;
    private MinerRepository minerRepository;

    private static final int MIN_PASSWD_LEN = 8;

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

        TextInputLayout ctrlUsernameLayout = findViewById(R.id.username_layout);
        ctrlUsernameLayout.setValidator((view, value) -> {
            // username must be email
            return Validator.isEmail(value);
        });
        binding.passwordLayout.setValidator((view, value) -> {
            // password must be longer than 8 chars
            return value != null && value.length() > MIN_PASSWD_LEN;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
        minerRepository = new MinerRepository();

    }

    public void onLogin(View view) {

        String username = Objects.requireNonNull(binding.username.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.login_password_required, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean rememberMe = binding.rememberMe.isChecked();

        binding.progressBar.setVisibility(View.VISIBLE);

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        final Activity self = this;
        Call<LoginServiceResponse> call = loginService.login(credentials);
        call.enqueue(new Callback<LoginServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginServiceResponse> call, @NonNull Response<LoginServiceResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        LoginServiceResponse resp = response.body();
                        if (resp != null) {
                            Session.key(resp.getKey(), rememberMe, self);
                            SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                            prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, true).apply();

                            minerRepository.getUserInfo(new OnCompleteListener<MinerDataResponse>() {
                                @Override
                                public void onComplete(MinerDataResponse value, Throwable error) {
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    if (error != null) {
                                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_SHORT).show();
                                        Debug.logException(error);
                                        return;
                                    }

                                    if (value != null) {
                                        Session.setUser(value.getUser());

                                        int registrationStatus = value.getRegistration_status();
                                        if (registrationStatus < 3/*not verified*/) {
                                            // we cannot continue before email is confirmed and
                                            // just display an information in a popup window
                                            showNotConfirmed();

                                        } else if (registrationStatus == 3) {
                                            // add referral code if exists and pay the enter fee
                                            startActivity(new Intent(self, ReferralActivity.class));

                                        } else {
                                            // go to the wallet
                                            startActivity(new Intent(self, WalletActivity.class));

                                        }
                                    }

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
            public void onFailure(@NonNull Call<LoginServiceResponse> call, @NonNull Throwable t) {
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
