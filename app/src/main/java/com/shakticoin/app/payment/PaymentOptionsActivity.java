package com.shakticoin.app.payment;

import android.app.Activity;
import android.content.Intent;
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
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.api.license.SubscribedLicenseModel;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shakticoin.app.api.license.LicenseTypeKt.compareLicenseType;

public class PaymentOptionsActivity extends DrawerActivity {
    public static int LIC_ACTION_NONE       = -1;
    public static int LIC_ACTION_APPLY      = 0;
    public static int LIC_ACTION_UPGRADE    = 1;
    public static int LIC_ACTION_DOWNGRADE  = 2;
    public static int LIC_ACTION_CANCEL     = 3;

    private ActivityPaymentOptionsBinding binding;
    private PaymentOptionsViewModel viewModel;
    private PageAdapter pages;

    private String licenseTypeId;
    private ArrayList<LicenseType> licenseTypesAll;
    private Map<String, ArrayList<LicenseType>> licenseTypesGrouped;
    private SubscribedLicenseModel currentSubscription;

    /** Plan type that user bought already. */
    private String existingPlanType;

    LicenseRepository licenseRepository = new LicenseRepository();

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
            existingPlanType = intent.getStringExtra(CommonUtil.prefixed("selectedPlanType"));
            licenseTypesAll = intent.getParcelableArrayListExtra(CommonUtil.prefixed("licenses"));
            currentSubscription = intent.getParcelableExtra(CommonUtil.prefixed("subscription"));
        } else {
            licenseTypeId = savedInstanceState.getString("licenseTypeId");
            existingPlanType = savedInstanceState.getString("selectedPlanType");
            licenseTypesAll = savedInstanceState.getParcelableArrayList("licenses");
            currentSubscription = savedInstanceState.getParcelable("subscription");
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
        outState.putString("selectedPlanType", existingPlanType);
        outState.putParcelable("subscription", currentSubscription);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMainAction(View v) {
        final Activity activity = this;
        String planCode = (String) v.getTag();
        LicenseType requestedLicenseType = null;
        for (LicenseType licenseType : licenseTypesAll) {
            if (licenseType.getPlanCode().equals(planCode)) {
                requestedLicenseType = licenseType;
                break;
            }
        }

        if (requestedLicenseType == null) return;

        // We should decide which method apply/upgrade/downgrade we must use for the operation.
        // The hierarchy follows the list M101/T100/T200/T300/T400
        final String requestedPlanCode = requestedLicenseType.getPlanCode();
        if (existingPlanType != null) {
            int comparisionResult = compareLicenseType(requestedLicenseType.getPlanType(), existingPlanType);
            if (comparisionResult < 0) {
                if (currentSubscription != null && currentSubscription.getSubscriptionId() != null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    licenseRepository.downgradeSubscription(
                            requestedPlanCode,
                            currentSubscription.getSubscriptionId(), new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(String targetUrl, Throwable error) {
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    if (error != null) {
                                        if (error instanceof UnauthorizedException) {
                                            startActivity(Session.unauthorizedIntent(activity));
                                        } else {
                                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    }

                                    Intent intent = new Intent(activity, PaymentFlowActivity.class);
                                    intent.putExtra(CommonUtil.prefixed("planCode"), requestedPlanCode);
                                    intent.putExtra(CommonUtil.prefixed("targetUrl"), targetUrl);
                                    startActivity(intent);
                                }
                            });
                }
            } else if (comparisionResult > 0) {
                if (currentSubscription != null && currentSubscription.getSubscriptionId() != null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    licenseRepository.upgradeSubscription(
                            requestedPlanCode,
                            currentSubscription.getSubscriptionId(), new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(String targetUrl, Throwable error) {
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    if (error != null) {
                                        if (error instanceof UnauthorizedException) {
                                            startActivity(Session.unauthorizedIntent(activity));
                                        } else {
                                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    }

                                    Intent intent = new Intent(activity, PaymentFlowActivity.class);
                                    intent.putExtra(CommonUtil.prefixed("planCode"), requestedPlanCode);
                                    intent.putExtra(CommonUtil.prefixed("targetUrl"), targetUrl);
                                    startActivity(intent);
                                }
                            });
                }
            } else {
                // TODO: user have this license already
            }
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            licenseRepository.checkoutSubscription(requestedPlanCode, new OnCompleteListener<String>() {
                @Override
                public void onComplete(String targetUrl, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        if (error instanceof UnauthorizedException) {
                            startActivity(Session.unauthorizedIntent(activity));
                        } else {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        }
                        return;
                    }

                    Intent intent = new Intent(activity, PaymentFlowActivity.class);
                    intent.putExtra(CommonUtil.prefixed("planCode"), requestedPlanCode);
                    intent.putExtra(CommonUtil.prefixed("targetUrl"), targetUrl);
                    startActivity(intent);
                }
            });
        }

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
            return PaymentOptionsPlanFragment.getInstance(licenseTypeId, existingPlanType, plan, licenseTypesGrouped.get(plan));
        }

        @Override
        public int getCount() {
            return licenseTypesGrouped.size();
        }
    }
}
