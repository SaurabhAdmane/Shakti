package com.shakticoin.app.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.R;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentOptionsActivity extends DrawerActivity {
    private ActivityPaymentOptionsBinding binding;
    private PaymentOptionsViewModel viewModel;
    private PageAdapter pages;

    private String licenseTypeId;
    private ArrayList<LicenseType> licenseTypesAll;
    private Map<String, ArrayList<LicenseType>> licenseTypesGrouped;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PaymentOptionsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_options);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        super.onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            licenseTypeId = intent.getStringExtra(CommonUtil.prefixed("licenseTypeId"));
            licenseTypesAll = intent.getParcelableArrayListExtra(CommonUtil.prefixed("licenses"));
        } else {
            licenseTypeId = savedInstanceState.getString("licenseTypeId");
            licenseTypesAll = savedInstanceState.getParcelableArrayList("licenses");
        }

        // group all license type by plan base code
        // {"M101" : [...], "T100" : [...], ...}
        if (licenseTypesAll != null && licenseTypesAll.size() > 0) {
            licenseTypesGrouped = new HashMap<>();
            for (PaymentOptionsViewModel.PackageType packageType : PaymentOptionsViewModel.PackageType.values()) {
                ArrayList<LicenseType> planGrp = new ArrayList<>();
                for (LicenseType type : licenseTypesAll) {
                    if (type.getPlanCode().startsWith(packageType.name())) {
                        planGrp.add(type);
                    }
                }
                licenseTypesGrouped.put(packageType.name(), planGrp);
            }
        }

        pages = new PageAdapter(getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.mainFragment.setAdapter(pages);

        // Set initially selected licence type and base plan (M101, T100, ... )
        PaymentOptionsViewModel.PackageType[] packageTypes = PaymentOptionsViewModel.PackageType.values();
        for (int i = 0; i < packageTypes.length; i++) {
            boolean nextIteration = true;
            String groupName = packageTypes[i].name();
            ArrayList<LicenseType> groupList = licenseTypesGrouped.get(groupName);
            if (groupList != null) {
                for (LicenseType type : groupList) {
                    if (licenseTypeId.equals(type.getId())) {
                        binding.mainFragment.setCurrentItem(i);
                        nextIteration = false;
                        break;
                    }
                }
            }
            if (!nextIteration) break;
        }

        // when the page is changed we need to maintain selected license type and plan in
        // order to get the fragment know which data to display
        binding.mainFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                PaymentOptionsViewModel.PackageType[] miningPlan = PaymentOptionsViewModel.PackageType.values();
                String plan = miningPlan[position].name();
                ArrayList<LicenseType> currentGroup = licenseTypesGrouped.get(plan);
                viewModel.selectedPackage.setValue(currentGroup.get(0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("licenseTypeId", licenseTypeId);
        outState.putParcelableArrayList("licenses", licenseTypesAll);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMainAction(View v) {
        final Activity activity = this;
    }

    /**
     * Open wallet and reset back stack.
     * It does not make sense to navigate back though the registration screens after the payment is
     * completed successfully.
     */
    private void openWallet() {
        Intent intent = new Intent(this, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onNext(View v) {
        int currentIndex = binding.mainFragment.getCurrentItem();
        if (currentIndex < (PaymentOptionsViewModel.PackageType.values().length - 1)) {
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

        PageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PaymentOptionsViewModel.PackageType[] miningPlan = PaymentOptionsViewModel.PackageType.values();
            String plan = miningPlan[position].name();
            return PaymentOptionsPlanFragment.getInstance(licenseTypeId, plan, licenseTypesGrouped.get(plan));
        }

        @Override
        public int getCount() {
            return licenseTypesGrouped.size();
        }
    }
}
