package com.shakticoin.app.settings;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.shakticoin.app.registration.RecoveryPasswordActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityResetPasswordBinding;

import com.shakticoin.app.wallet.BaseWalletActivity;

public class SettingsPasswordActivity extends BaseWalletActivity {
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_password_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onResetPassword(View v) {
        startActivity(new Intent(this, RecoveryPasswordActivity.class));
    }
}
