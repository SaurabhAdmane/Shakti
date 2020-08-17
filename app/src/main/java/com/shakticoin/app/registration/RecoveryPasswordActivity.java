package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Validator;

import java.util.Objects;

public class RecoveryPasswordActivity extends AppCompatActivity {
    private RecoveryPasswordModel viewModel;
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

        // FIXME: not implemented
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
