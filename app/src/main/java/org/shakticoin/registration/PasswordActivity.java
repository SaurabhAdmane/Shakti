package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.databinding.ActivityPasswordBinding;
import org.shakticoin.util.CommonUtil;


public class PasswordActivity extends AppCompatActivity {
    public static final String KEY_PHONE_NUMBER     = "phoneNumber";
    public static final String KEY_EMAIL_ADDR       = "emailAddress";
    public static final String KEY_POSTAL_CODE      = "postalCode";
    public static final String KEY_COUNTRY_CODE     = "countryCode";

    ProgressBar progressBar;

    private PasswordActivityModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(PasswordActivityModel.class);
        binding.setViewModel(viewModel);
        binding.confirmPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createUser();
            }
            return false;
        });

        Intent intent = getIntent();
        viewModel.setPhoneNumber(intent.getStringExtra(CommonUtil.prefixed(KEY_PHONE_NUMBER, this)));
        viewModel.setEmailAddress(intent.getStringExtra(CommonUtil.prefixed(KEY_EMAIL_ADDR, this)));
        viewModel.setPostalCode(intent.getStringExtra(CommonUtil.prefixed(KEY_POSTAL_CODE, this)));
        viewModel.setCurrentCountry(intent.getStringExtra(CommonUtil.prefixed(KEY_COUNTRY_CODE, this)));

        progressBar = findViewById(R.id.progressBar);
    }

    public void onSave(View view) {
        createUser();
    }

    public void onCancel(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void createUser() {
        String newPassword = viewModel.newPassword.getValue();
        String verifyPassword = viewModel.verifyPassword.getValue();
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(verifyPassword) || !newPassword.equals(verifyPassword)) {
            Toast.makeText(this, R.string.err_incorrect_password, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        viewModel.onRegister(new OnCompleteListener() {
            @Override
            public void onComplete(Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
