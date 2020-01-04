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
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.api.payment.PaymentRepository;
import com.shakticoin.app.databinding.ActivityPaymentOptionsBinding;
import com.shakticoin.app.payment.stripe.StripeActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.shakticoin.app.wallet.WalletActivity;

public class PaymentOptionsActivity extends BaseWalletActivity {
    public static final int STRIPE_PAYMENT  = 100;

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
        final Activity activity = this;

        // pay the order
        Intent intent = new Intent(activity, StripeActivity.class);
        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_ID, activity), "XXX"/*value.getId()*/);
        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_AMOUNT, activity), 10.0/*value.getAmount()*/);
        intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_NAME, activity),
                String.format("%1$s - %2$s", "M101", "Tier description"));
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
                userRepository.updateRegistrationStatus(Constants.RegistrationStatus.REGST_COMPL, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Void value, Throwable error) {
//                        binding.progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        openWallet();
                    }
                });
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
