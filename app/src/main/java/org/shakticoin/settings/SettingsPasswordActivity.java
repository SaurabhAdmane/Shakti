package org.shakticoin.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityHelpBinding;
import org.shakticoin.databinding.ActivityResetPasswordBinding;
import org.shakticoin.registration.RecoveryPasswordActivity;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Validator;
import org.shakticoin.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsPasswordActivity extends BaseWalletActivity {
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_password_title), true);
    }

    public void onResetPassword(View v) {
        startActivity(new Intent(this, RecoveryPasswordActivity.class));
    }
}
