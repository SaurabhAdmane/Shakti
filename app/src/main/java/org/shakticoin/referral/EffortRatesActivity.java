package org.shakticoin.referral;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityEffortRatesBinding;
import org.shakticoin.wallet.BaseWalletActivity;

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
}
