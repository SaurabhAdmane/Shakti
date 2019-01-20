package org.shakticoin.vault;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityVaultAdvantageBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class VaultAdvantageActivity extends BaseWalletActivity {
    private ActivityVaultAdvantageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_advantage);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.vault_title));
    }
}
