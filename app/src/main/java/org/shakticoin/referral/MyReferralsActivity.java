package org.shakticoin.referral;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityMyReferralsBinding;
import org.shakticoin.wallet.BaseWalletActivity;

@SuppressLint("Registered")
public class MyReferralsActivity extends BaseWalletActivity {
    private ActivityMyReferralsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_referrals);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.my_refs_title));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MyReferralsSummaryFragment())
                .commit();
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }
}
