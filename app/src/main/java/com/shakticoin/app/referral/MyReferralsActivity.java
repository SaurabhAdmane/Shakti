package com.shakticoin.app.referral;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityMyReferralsBinding;
import com.shakticoin.app.widget.DrawerActivity;

@SuppressLint("Registered")
public class MyReferralsActivity extends DrawerActivity {
    private ActivityMyReferralsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_referrals);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.my_refs_title));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MyReferralsLockedFragment())
                .commit();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }

    public void onAddReferral(View v) {
        Intent intent = new Intent(this, AddReferralActivity.class);
        startActivity(intent);
    }

    public void onSeeRates(View v) {
        Intent intent = new Intent(this, EffortRatesActivity.class);
        startActivity(intent);
    }

    public void onStartReferring(View v) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MyReferralsSummaryFragment())
                .addToBackStack(MyReferralsSummaryFragment.class.getSimpleName())
                .commit();
    }
}
