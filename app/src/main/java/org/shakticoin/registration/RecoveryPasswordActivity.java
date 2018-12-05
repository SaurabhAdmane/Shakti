package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.shakticoin.BuildConfig;
import org.shakticoin.R;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.auth.LoginService;
import org.shakticoin.api.auth.PasswordResetRequest;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Debug;
import org.shakticoin.util.Validator;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecoveryPasswordActivity extends AppCompatActivity {
    private RecoveryPasswordModel viewModel;
    private LoginService loginService;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().hide(Objects.requireNonNull(manager.findFragmentById(R.id.sent_fragment))).commit();

        progressBar = findViewById(R.id.progressBar);

        viewModel = ViewModelProviders.of(this).get(RecoveryPasswordModel.class);

        // user may entered email in login activity
        Intent intent = getIntent();
        String emailAddressKey = CommonUtil.prefixed("emailAddress", this);
        if (intent.hasExtra(emailAddressKey)) {
            viewModel.emailAddress.setValue(intent.getStringExtra(emailAddressKey));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
    }

    private void nextPage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        tx.hide(Objects.requireNonNull(manager.findFragmentById(R.id.email_fragment)));
        tx.show(Objects.requireNonNull(manager.findFragmentById(R.id.sent_fragment)));
        tx.commit();
        viewModel.setRequestSent(true);
    }

    public void onCancel(View view) {
        progressBar.setVisibility(View.INVISIBLE);
        NavUtils.navigateUpFromSameTask(this);
    }

    public void onSend(View view) {
        resetPassword();
    }

    public void resetPassword() {
        String emailAddress = viewModel.getEmail();

        if (!Validator.isEmail(emailAddress)) {
            viewModel.emailAddressErrMsg.setValue(getString(R.string.err_email_required));
            return;
        }

        final Activity self = this;

        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = loginService.reset(new PasswordResetRequest(emailAddress));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        if (!viewModel.isRequestSent()) nextPage();
                    } else {
                        Toast.makeText(self,
                                BuildConfig.DEBUG ? response.message() : getString(R.string.err_unexpected),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(self, Debug.getFailureMsg(self, t), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                Debug.logException(t);
            }
        });
    }
}
