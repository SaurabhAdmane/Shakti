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
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentOptionsActivity extends DrawerActivity {
    public static final int STRIPE_PAYMENT  = 100;

    private ActivityPaymentOptionsBinding binding;
    private PaymentOptionsViewModel viewModel;
    private PageAdapter pages;

//    private VaultRepository vaultRepository = new VaultRepository();
    private LicenseRepository licenceRepository = new LicenseRepository();
//    private List<PackageExtended> packages;

//    private int vaultId = -1;
    private String licenseTypeId;
    private ArrayList<LicenseType> licenseTypesAll;
    private Map<String, ArrayList<LicenseType>> licenseTypesGrouped;
//    private int packageId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PaymentOptionsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_options);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        super.onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        final Activity self = this;

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            licenseTypeId = intent.getStringExtra(CommonUtil.prefixed("licenseTypeId", this));
            licenseTypesAll = intent.getParcelableArrayListExtra(CommonUtil.prefixed("licenses", this));
//            vaultId = intent.getIntExtra(CommonUtil.prefixed("vaultId", this), -1);
//            packageId = intent.getIntExtra(CommonUtil.prefixed("packageId", this), -1);
        } else {
            licenseTypeId = savedInstanceState.getString("licenseTypeId");
            licenseTypesAll = savedInstanceState.getParcelableArrayList("licenses");
//            vaultId = savedInstanceState.getInt("vaultId", -1);
//            packageId = savedInstanceState.getInt("packageId", -1);
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
//        outState.putInt("vaultId", vaultId);
//        outState.putInt("packageId", packageId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMainAction(View v) {
        final Activity activity = this;

//        PackagePlanExtended plan = viewModel.selectedPlan.get();
//        PackageExtended pkg = viewModel.selectedPackage.getValue();
//        // pay the order
//        Intent intent = new Intent(activity, StripeActivity.class);
//        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_AMOUNT, activity), plan.getFiat_price());
//        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_NAME, activity),
//                String.format("%1$s - %2$s", "M101", "Tier description"));
//        intent.putExtra(CommonUtil.prefixed("packageName", this), pkg.getName());
//        intent.putExtra(CommonUtil.prefixed("period", this), plan.getPeriod());
//        startActivityForResult(intent, STRIPE_PAYMENT);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == STRIPE_PAYMENT) {
//            switch (resultCode) {
//                case RESULT_OK:
//                    if (data != null) {
//                        long orderId = data.getLongExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_ID, this), -1);
//                        completePayment(data.getStringExtra(CommonUtil.prefixed(StripeActivity.KEY_TOKEN, this)), orderId);
//                    }
//                    break;
//                case RESULT_CANCELED:
//                    Toast.makeText(this, R.string.err_payment_cancelled, Toast.LENGTH_SHORT).show();
//                    openWallet();
//                    break;
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    private void completePayment(@Nullable String token, long orderId) {
//        if (token == null) return; // perhaps not possible
//        if (orderId < 0) return;
//
//        final Activity activity = this;
//
////        binding.progressBar.setVisibility(View.VISIBLE);
//        PaymentRepository repository = new PaymentRepository();
//        repository.makeStripePayment(orderId, token, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(Void value, Throwable error) {
//                if (error != null) {
//                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // we can consider the registration completed at this point
//                UserRepository userRepository = new UserRepository();
////                userRepository.updateRegistrationStatus(Constants.RegistrationStatus.REGST_COMPL, new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(Void value, Throwable error) {
//////                        binding.progressBar.setVisibility(View.INVISIBLE);
////                        if (error != null) {
////                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////
////                        openWallet();
////                    }
////                });
//            }
//        });
//    }

    /**
     * Open wallet and reset back stack.
     * It does not make sense to navigate back though the registration screens after the payment is
     * completed successfully.
     */
//    private void openWallet() {
//        Intent intent = new Intent(this, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }

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
//        private List<PackageExtended> packages;

        PageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
//            this.packages = packages;
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
