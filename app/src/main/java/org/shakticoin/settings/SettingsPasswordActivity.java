package org.shakticoin.settings;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityResetPasswordBinding;
import org.shakticoin.registration.RecoveryPasswordActivity;
import org.shakticoin.wallet.BaseWalletActivity;

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
