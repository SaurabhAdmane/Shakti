package com.shakticoin.app.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycUserView;
import com.shakticoin.app.databinding.ActivitySettingsBinding;
import com.shakticoin.app.profile.ProfileActivity;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

public class SettingsActivity extends DrawerActivity {
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;
    private KYCRepository kycRepository = new KYCRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.settings_title));

        binding.versionName.setText(getString(R.string.settings_version, BuildConfig.VERSION_NAME));
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.progressBar.setVisibility(View.VISIBLE);
        kycRepository.getUserDetails(new OnCompleteListener<KycUserView>() {
            @Override
            public void onComplete(KycUserView userinfo, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Context context = ShaktiApplication.getContext();
                    Toast.makeText(context, Debug.getFailureMsg(context, error), Toast.LENGTH_LONG).show();
                    return;
                }

                binding.fullName.setText((String) userinfo.getFullName());
                binding.emailAddress.setText((String) userinfo.getPrimaryEmail());
                binding.kycStatus.setText((String) userinfo.getVerificationStatus());
            }
        }, false);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onOpenPersonalInfo(View v) {
        startActivity(new Intent(this, SettingsPersonalActivity.class));
    }

    public void onKYCVerification(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("showStatus", true);
        startActivity(intent);
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
        Session.clean();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
