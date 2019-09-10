package com.shakticoin.app.referral;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityEffortRatesBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class EffortRatesActivity extends BaseWalletActivity {
    private ActivityEffortRatesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_effort_rates);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.effort_rates_title), true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new EffortRatesListFragment())
                .commit();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }
}