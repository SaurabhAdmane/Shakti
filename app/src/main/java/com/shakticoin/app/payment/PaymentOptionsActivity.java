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
import com.shakticoin.app.api.payment.PaymentRepository;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.api.vault.PackageExtended;
import com.shakticoin.app.api.vault.PackagePlanExtended;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.payment.stripe.StripeActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.shakticoin.app.wallet.WalletActivity;

import java.util.List;

public class PaymentOptionsActivity extends BaseWalletActivity {
    public static final int STRIPE_PAYMENT  = 100;

    private ActivityPaymentOptionsBinding binding;
    private PaymentOptionsViewModel viewModel;
    private PageAdapter pages;

    private VaultRepository vaultRepository = new VaultRepository();
    private List<PackageExtended> packages;

    private int vaultId = -1;
    private int packageId = -1;

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
            vaultId = intent.getIntExtra(CommonUtil.prefixed("vaultId", this), -1);
            packageId = intent.getIntExtra(CommonUtil.prefixed("packageId", this), -1);
        } else {
            vaultId = savedInstanceState.getInt("vaultId", -1);
            packageId = savedInstanceState.getInt("packageId", -1);
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        vaultRepository.getVaultPackages(vaultId, new OnCompleteListener<List<PackageExtended>>() {
            @Override
            public void onComplete(List<PackageExtended> value, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    }
                    return;
                }

                packages = value;
                pages = new PageAdapter(getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                        packages);
                binding.mainFragment.setAdapter(pages);
                PaymentOptionsViewModel.PackageType[] packageTypes = PaymentOptionsViewModel.PackageType.values();
                for (int i = 0; i < packages.size(); i++) {
                    PackageExtended pkg = packages.get(i);
                    if (packageId == pkg.getId()) {
                        viewModel.selectedPackage.setValue(pkg);
                        viewModel.selectedPackageType.setValue(packageTypes[i]);
                        break;
                    }
                }
            }
        });

        binding.mainFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                viewModel.selectedPackage.setValue(packages.get(position));
                PaymentOptionsViewModel.PackageType[] packageTypes = PaymentOptionsViewModel.PackageType.values();
                viewModel.selectedPackageType.setValue(packageTypes[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("vaultId", vaultId);
        outState.putInt("packageId", packageId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMainAction(View v) {
        final Activity activity = this;

        PackagePlanExtended plan = viewModel.selectedPlan.get();
        PackageExtended pkg = viewModel.selectedPackage.getValue();
        // pay the order
        Intent intent = new Intent(activity, StripeActivity.class);
        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_AMOUNT, activity), plan.getFiat_price());
        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_NAME, activity),
                String.format("%1$s - %2$s", "M101", "Tier description"));
        intent.putExtra(CommonUtil.prefixed("packageName", this), pkg.getName());
        intent.putExtra(CommonUtil.prefixed("period", this), plan.getPeriod());
        startActivityForResult(intent, STRIPE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == STRIPE_PAYMENT) {
            switch (resultCode) {
                case RESULT_OK:
                    if (data != null) {
                        long orderId = data.getLongExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_ID, this), -1);
                        completePayment(data.getStringExtra(CommonUtil.prefixed(StripeActivity.KEY_TOKEN, this)), orderId);
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, R.string.err_payment_cancelled, Toast.LENGTH_SHORT).show();
                    openWallet();
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void completePayment(@Nullable String token, long orderId) {
        if (token == null) return; // perhaps not possible
        if (orderId < 0) return;

        final Activity activity = this;

//        binding.progressBar.setVisibility(View.VISIBLE);
        PaymentRepository repository = new PaymentRepository();
        repository.makeStripePayment(orderId, token, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                    return;
                }

                // we can consider the registration completed at this point
                UserRepository userRepository = new UserRepository();
//                userRepository.updateRegistrationStatus(Constants.RegistrationStatus.REGST_COMPL, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(Void value, Throwable error) {
////                        binding.progressBar.setVisibility(View.INVISIBLE);
//                        if (error != null) {
//                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        openWallet();
//                    }
//                });
            }
        });
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
        private List<PackageExtended> packages;

        PageAdapter(@NonNull FragmentManager fm, int behavior, List<PackageExtended> packages) {
            super(fm, behavior);
            this.packages = packages;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PaymentOptionsViewModel.PackageType[] miningPlan = PaymentOptionsViewModel.PackageType.values();
            return PaymentOptionsPlanFragment.getInstance(vaultId, miningPlan[position].name(), packages.get(position));
        }

        @Override
        public int getCount() {
            return packages.size();
        }
    }
}
