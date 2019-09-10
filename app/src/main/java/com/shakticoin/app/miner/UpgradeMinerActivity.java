package com.shakticoin.app.miner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.wallet.BaseWalletActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityUpgradeMinerBinding;

public class UpgradeMinerActivity extends BaseWalletActivity {
    private ActivityUpgradeMinerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade_miner);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }
}