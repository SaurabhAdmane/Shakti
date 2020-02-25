package com.shakticoin.app.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityCompanyProfileBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CompanyProfileActivity extends BaseWalletActivity {
    private ActivityCompanyProfileBinding binding;
    private CompanyProfileViewModel viewModel;
    private CompanyProfileFragmentAdapter pageAdapter;

    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(CompanyProfileViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.profile_company_title));

        toolbarTitle = binding.getRoot().findViewById(R.id.toolbarTitle);

        String[] pageIndicatorItems = new String[] {
                getString(R.string.wallet_page_personal),
                null,
                getString(R.string.wallet_page_additional),
                null,
                getString(R.string.wallet_page_kyc)
        };
        binding.pageIndicator.setSizeAndLabels(pageIndicatorItems);
        binding.pageIndicator.setSelectedIndex(1);

        pageAdapter = new CompanyProfileFragmentAdapter(getSupportFragmentManager());
        binding.mainFragment.setAdapter(pageAdapter);
        binding.mainFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                binding.pageIndicator.setSelectedIndex(position+1);
                if (position == 0 || position == 1) {
                    toolbarTitle.setText(R.string.profile_company_title);
                    binding.profileBackground.setBackgroundResource(R.drawable.personal_background);
                } else if (position == 2 || position == 3) {
                    toolbarTitle.setText(R.string.wallet_page_additional);
                    binding.profileBackground.setBackgroundResource(R.drawable.additional_background);
                } else {
                    toolbarTitle.setText(R.string.wallet_page_kyc);
                    binding.profileBackground.setBackgroundResource(R.drawable.kyc_validation_background);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onSetDateEstablished(View v) {
        DatePicker picker = new DatePicker((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            viewModel.establishmentDate.setValue(fmt.format(cal.getTime()));
        });
        picker.show(getSupportFragmentManager(), "date-picker");

    }

    public void onNextCompanyInfoPage(View v) {
        binding.mainFragment.setCurrentItem(1, true);
    }

    public void onUpdateCompanyInfo(View v) {
        binding.mainFragment.setCurrentItem(2, true);
    }

    public void onNextCompanyAdditionalPage(View v) {
        binding.mainFragment.setCurrentItem(3, true);
    }

    public void onUpdateCompanyAdditionalInfo(View v) {
        binding.mainFragment.setCurrentItem(4, true);
    }

    public void onAddAnotherCompany(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onCancel(View v) {
        navigateUpTo(new Intent(this, WalletActivity.class));
    }

    class CompanyProfileFragmentAdapter extends FragmentPagerAdapter {

        public CompanyProfileFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CompanyInfoFragment1();
                case 1:
                    return new CompanyInfoFragment2();
                case 2:
                    return new CompanyAdditionalInfoFragment1();
                case 3:
                    return new CompanyAdditionalInfoFragment2();
                default:
                    return new KycSelectorFragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
