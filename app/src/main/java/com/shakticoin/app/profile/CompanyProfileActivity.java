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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityCompanyProfileBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CompanyProfileActivity extends BaseWalletActivity {
    private ActivityCompanyProfileBinding binding;
    private CompanyProfileViewModel viewModel;

    private final String[] tags = new String[] {
            CompanyInfoFragment1.class.getSimpleName(),
            CompanyInfoFragment2.class.getSimpleName(),
            CompanyAdditionalInfoFragment1.class.getSimpleName(),
            CompanyAdditionalInfoFragment2.class.getSimpleName(),
            KycSelectorFragment.class.getSimpleName()};
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
                getString(R.string.profile_company_page_company_info),
                null,
                getString(R.string.wallet_page_additional),
                null,
                getString(R.string.wallet_page_kyc)
        };
        binding.pageIndicator.setSizeAndLabels(pageIndicatorItems);
        binding.pageIndicator.setSelectedIndex(1);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment != null && fragment.isVisible()) {
                    binding.pageIndicator.setSelectedIndex(i+1);
                }
            }
        });
        pageAdapter = new CompanyProfileFragmentAdapter(getSupportFragmentManager());
        selectPage(0);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    private void selectPage(int index) {
        Fragment fragment = pageAdapter.getItem(index);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index > 0) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(binding.mainFragment.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        binding.pageIndicator.setSelectedIndex(index+1);

        if (index == 0 || index == 1) {
            toolbarTitle.setText(R.string.profile_company_title);
            binding.profileBackground.setBackgroundResource(R.drawable.personal_background);
        } else if (index == 2 || index == 3) {
            toolbarTitle.setText(R.string.wallet_page_additional);
            binding.profileBackground.setBackgroundResource(R.drawable.additional_background);
        } else {
            toolbarTitle.setText(R.string.wallet_page_kyc);
            binding.profileBackground.setBackgroundResource(R.drawable.kyc_validation_background);
        }
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

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            viewModel.establishmentDate.setValue(fmt.format(cal.getTime()));
        });
        picker.show(getSupportFragmentManager(), "date-picker");

    }

    public void onNextCompanyInfoPage(View v) {
        selectPage(1);
    }

    public void onUpdateCompanyInfo(View v) {
        selectPage(2);
    }

    public void onNextCompanyAdditionalPage(View v) {
        selectPage(3);
    }

    public void onUpdateCompanyAdditionalInfo(View v) {
        selectPage(4);
    }

    public void onAddAnotherCompany(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onCancel(View v) {
        navigateUpTo(new Intent(this, WalletActivity.class));
    }

    static class CompanyProfileFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        CompanyProfileFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>(5);
            fragments.add(new CompanyInfoFragment1());
            fragments.add(new CompanyInfoFragment2());
            fragments.add(new CompanyAdditionalInfoFragment1());
            fragments.add(new CompanyAdditionalInfoFragment2());
            fragments.add(new KycSelectorFragment());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position >= fragments.size()) throw new IllegalArgumentException();
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
