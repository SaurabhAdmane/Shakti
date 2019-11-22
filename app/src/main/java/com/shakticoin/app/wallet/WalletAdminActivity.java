package com.shakticoin.app.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityWalletAdminBinding;

import java.util.Objects;

public class WalletAdminActivity extends BaseWalletActivity {
    ActivityWalletAdminBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_admin);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }
}
