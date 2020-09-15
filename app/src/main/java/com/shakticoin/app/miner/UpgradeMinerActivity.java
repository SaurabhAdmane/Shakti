package com.shakticoin.app.miner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityUpgradeMinerBinding;
import com.shakticoin.app.widget.DrawerActivity;

public class UpgradeMinerActivity extends DrawerActivity {
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

    public void onUpgrade(View v) {
        startActivity(new Intent(this, MiningLicenseActivity.class));
    }
}
