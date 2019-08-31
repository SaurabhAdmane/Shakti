package com.shakticoin.app.referral;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityAddReferralBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class AddReferralActivity extends BaseWalletActivity {
    private ActivityAddReferralBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_referral);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.my_refs_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }
}
