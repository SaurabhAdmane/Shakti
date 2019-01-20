package org.shakticoin.vault;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityVaultChooserBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class VaultChooserActivity extends BaseWalletActivity {
    private ActivityVaultChooserBinding binding;
    private VaultChooserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_chooser);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(VaultChooserViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.vault_title), true);
    }

    public void onProceedWithVault(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
