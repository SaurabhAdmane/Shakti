package com.shakticoin.app.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityProfileBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class ProfileActivity extends BaseWalletActivity {
    private ActivityProfileBinding binding;
    private PersonalViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.profile_personal_title));

        binding.pageIndicator.setSelectedIndex(1);

        Activity activity = this;
        binding.pager.setAdapter(new ProfileFragmentAdapter(getSupportFragmentManager()));
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                Debug.logDebug(Integer.toString(i));
                binding.pageIndicator.setSelectedIndex(i+1);
                if (i == 0 || i == 1) {
                    binding.profileBackground.setBackgroundResource(R.drawable.personal_background);
                } else if (i == 2 || i == 3) {
                    binding.profileBackground.setBackgroundResource(R.drawable.additional_background);
                } else {
                    binding.profileBackground.setBackgroundResource(R.drawable.kyc_validation_background);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onDoKYC(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onUpdatePersonalInfo(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onUpdateAdditionalInfo(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onNextPersonalInfo(View v) {
        binding.pager.setCurrentItem(1);
    }

    public void onNextAdditionalInfo(View v) {
        binding.pager.setCurrentItem(3);
    }

    public void onCancel(View v) {
        finish();
    }

    /** Collection of fragments for the activity */
    class ProfileFragmentAdapter extends FragmentPagerAdapter {

        public ProfileFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new PersonalInfoFragment1();
                case 1:
                    return new PersonalInfoFragment2();
                case 2:
                    return new AdditionalInfoFragment1();
                case 3:
                    return new AdditionalInfoFragment2();
                case 4:
                    return new KycSelectorFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
