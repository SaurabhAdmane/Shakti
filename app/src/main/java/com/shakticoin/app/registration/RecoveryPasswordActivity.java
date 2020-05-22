package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.auth.LoginService;
import com.shakticoin.app.api.auth.PasswordResetRequest;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;

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
                .baseUrl(BaseUrl.BASE_URL)
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
        UserRepository userRepo = new UserRepository();
        userRepo.resetPassword(emailAddress, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!viewModel.isRequestSent()) nextPage();
            }
        });
    }
}
