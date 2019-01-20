package org.shakticoin.profile;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityProfileBinding;
import org.shakticoin.util.Debug;
import org.shakticoin.wallet.BaseWalletActivity;

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
