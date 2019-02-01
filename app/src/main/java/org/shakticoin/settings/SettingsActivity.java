package org.shakticoin.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivitySettingsBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class SettingsActivity extends BaseWalletActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_title));
    }

    public void onOpenPersonalInfo(View v) {
        startActivity(new Intent(this, SettingsPersonalActivity.class));
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
}
