package org.shakticoin.miner;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityUpgradeMinerBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class UpgradeMinerActivity extends BaseWalletActivity {
    private ActivityUpgradeMinerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade_miner);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));
    }
}
