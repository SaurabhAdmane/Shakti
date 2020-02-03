package com.shakticoin.app.vault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.vault.VaultExtended;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityVaultChooserBinding;
import com.shakticoin.app.profile.CompanyProfileActivity;
import com.shakticoin.app.registration.MiningLicenseActivity;
import com.shakticoin.app.settings.SettingsContactUsActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.util.List;

public class VaultChooserActivity extends BaseWalletActivity {
    private ActivityVaultChooserBinding binding;
    private VaultChooserViewModel viewModel;

    private OnOptionChanged optionChangedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_chooser);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(VaultChooserViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.vault_title), true);

        final AppCompatActivity self = this;
        VaultRepository vaultRepo = new VaultRepository();
        binding.progressBar.setVisibility(View.VISIBLE);
        vaultRepo.getVaults(new OnCompleteListener<List<VaultExtended>>() {
            @Override
            public void onComplete(List<VaultExtended> vaults, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    } else {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                optionChangedListener = new OnOptionChanged();
                viewModel.selectedVault.addOnPropertyChangedCallback(optionChangedListener);

                viewModel.init(vaults);
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 1;
    }

    public void onProceedWithVault(View v) {
        VaultExtended selectedVault = viewModel.selectedVault.get();
        if (selectedVault != null) {
            String transition = selectedVault.getTransition_to_view();
            if (VaultExtended.PACKAGE_VIEW.equals(transition)) {

                Intent intent = new Intent(this, MiningLicenseActivity.class);
                intent.putExtra(CommonUtil.prefixed("vaultId", this), selectedVault.getId());
                startActivity(intent);

            } if (VaultExtended.COMPANY_INFO_VIEW.equals(transition)) {

                Intent intent = new Intent(this, CompanyProfileActivity.class);
                startActivity(intent);

            } else if (VaultExtended.CONTACT_US_VIEW.equals(transition)) {

                Intent intent = new Intent(this, SettingsContactUsActivity.class);
                startActivity(intent);

            } else {
                // other transitions are not implemented yet
                Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String formatFeatureList(List<String> features) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p><b>").append(getString(R.string.minerlic_features)).append("</b></p>");
        if (features != null && features.size() > 0) {
            for (String feature : features) {
                sb.append("<p>").append("\u2022 ").append(feature).append("</p>");
            }
        }
        return sb.toString();

    }

    class OnOptionChanged extends Observable.OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            VaultExtended vault = ((ObservableField<VaultExtended>) sender).get();
            if (vault != null) {
                    binding.optionFeatures.setText(
                            HtmlCompat.fromHtml(formatFeatureList(vault.getFeatures()), HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                binding.optionFeatures.setText(null);
            }
        }
    }
}
