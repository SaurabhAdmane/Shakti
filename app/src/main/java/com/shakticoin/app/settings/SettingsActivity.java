package com.shakticoin.app.settings;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.shakticoin.app.registration.SignInActivity;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.databinding.ActivitySettingsBinding;
import com.shakticoin.app.profile.KycActivity;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class SettingsActivity extends BaseWalletActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_title));

        binding.versionName.setText(getString(R.string.settings_version, BuildConfig.VERSION_NAME));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onOpenPersonalInfo(View v) {
        startActivity(new Intent(this, SettingsPersonalActivity.class));
    }

    public void onKYCVerification(View v) {
        startActivity(new Intent(this, KycActivity.class));
    }

    public void onOpenHelp(View v) {
        startActivity(new Intent(this, SettingsHelpActivity.class));
    }

    public void onOpenApplicationTerms(View v) {
        startActivity(new Intent(this, SettingsAppTermsActivity.class));
    }

    public void onOpenPrivacyPolicy(View v) {
        startActivity(new Intent(this, SettingsPrivPolicyActivity.class));
    }

    public void onOpenTaxesInfo(View v) {
        startActivity(new Intent(this, SettingsTaxesActivity.class));
    }

    public void onOpenProcessingFees(View v) {
        startActivity(new Intent(this, SettingsProcFeeActivity.class));
    }

    public void onOpenContactUs(View v) {
        startActivity(new Intent(this, SettingsContactUsActivity.class));
    }

    public void onResetPassword(View v) {
        startActivity(new Intent(this, SettingsPasswordActivity.class));
    }

    public void onLogOut(View v) {
        Session.clean(this);
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
