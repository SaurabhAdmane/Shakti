package com.shakticoin.app.vault;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.wallet.BaseWalletActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityVaultChooserBinding;

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

    @Override
    protected int getCurrentDrawerSelection() {
        return 1;
    }

    public void onProceedWithVault(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
