package com.shakticoin.app.vault;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.vault.VaultExtended;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityVaultChooserBinding;
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
        vaultRepo.getVaults(new OnCompleteListener<List<VaultExtended>>() {
            @Override
            public void onComplete(List<VaultExtended> value, Throwable error) {
                if (error != null) {
                    Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    return;
                }

                optionChangedListener = new OnOptionChanged();
                viewModel.selectedVault.addOnPropertyChangedCallback(optionChangedListener);

                viewModel.init(value);
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 1;
    }

    public void onProceedWithVault(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    class OnOptionChanged extends Observable.OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            VaultExtended vault = ((ObservableField<VaultExtended>) sender).get();
            if (vault != null) {
                List<String> features = vault.getFeatures();
                if (features != null && features.size() > 0) {
                    StringBuilder sb = new StringBuilder(getString(R.string.vault_chs_features) + "\n\n");
                    for (int i = 0; i < features.size(); i++) {
                        String featureText = features.get(i);
                        sb.append("\u2022 ").append(featureText);
                        if (i < features.size() - 1) {
                            sb.append("\n\n");
                        }
                    }
                    binding.optionFeatures.setText(sb.toString());
                    return;
                }
            }
            binding.optionFeatures.setText(null);
        }
    }
}
