package com.shakticoin.app.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class PaymentOptionsActivity extends BaseWalletActivity {
    private ActivityPaymentOptionsBinding binding;
    private PaymentOptionsViewModel viewModel;
    private PageAdapter pages;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PaymentOptionsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_options);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        super.onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        pages = new PageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.mainFragment.setAdapter(pages);
        binding.mainFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                viewModel.selectedPlan.setValue(PaymentOptionsViewModel.Plan.values()[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        viewModel.selectedPlan.setValue(PaymentOptionsViewModel.Plan.values()[0]);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMainAction(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onNext(View v) {
        int currentIndex = binding.mainFragment.getCurrentItem();
        if (currentIndex < (PaymentOptionsViewModel.Plan.values().length - 1)) {
            binding.mainFragment.setCurrentItem(currentIndex + 1, true);
        }
    }

    public void onPrev(View v) {
        int currentIndex = binding.mainFragment.getCurrentItem();
        if (currentIndex > 0) {
            binding.mainFragment.setCurrentItem(currentIndex - 1, true);
        }
    }

    class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PaymentOptionsViewModel.Plan[] miningPlan = PaymentOptionsViewModel.Plan.values();
            return PaymentOptionsPlanFragment.getInstance(miningPlan[position].name());
        }

        @Override
        public int getCount() {
            return PaymentOptionsViewModel.Plan.values().length;
        }
    }
}
