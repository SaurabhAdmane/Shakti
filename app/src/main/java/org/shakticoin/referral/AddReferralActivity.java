package org.shakticoin.referral;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityAddReferralBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class AddReferralActivity extends BaseWalletActivity {
    private ActivityAddReferralBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_referral);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.my_refs_title), true);
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }
}
