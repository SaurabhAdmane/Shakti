package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.miner.MinerRepository;
import com.shakticoin.app.api.order.Order;
import com.shakticoin.app.api.order.OrderRepository;
import com.shakticoin.app.api.payment.PaymentRepository;
import com.shakticoin.app.api.tier.Tier;
import com.shakticoin.app.databinding.ActivityMiningLicenseBinding;
import com.shakticoin.app.payment.stripe.StripeActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;

import java.math.BigDecimal;

public class MiningLicenseActivity extends AppCompatActivity {
    public static final int STRIPE_PAYMENT  = 100;

    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;
    private OrderRepository orderRepository;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mining_license);
        final ViewGroup root = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        binding = ActivityMiningLicenseBinding.bind(root);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(MiningLicenseModel.class);
        binding.setViewModel(viewModel);

        viewModel.init(getIntent().getParcelableArrayListExtra("tiersList"));

        viewModel.selectedPlan.observe(this, plan -> {
            if (plan == null) return;
            switch (plan) {
                case M101:
                    updateDetailsM101();
                    break;
                case T100:
                    updateDetailsT100();
                    break;
                case T200:
                    updateDetailsT200();
                    break;
                case T300:
                    updateDetailsT300();
                    break;
            }
        });

        orderRepository = new OrderRepository();
    }

    private void updateDetails() {
        MiningLicenseModel.Plan planName = viewModel.selectedPlan.getValue();
        if (planName != null) {
            binding.license.setText(getString(R.string.minerlic_license, planName));
            binding.onClaim.setText(getString(R.string.minerlic_action, planName));
        }

        BigDecimal price = viewModel.getPaymentAmount();
        binding.paymentAmount.setText(price != null ?
                String.format(getResources().getConfiguration().locale, "$%1$.2f", price) : "");

    }
    private void updateDetailsM101() {
        updateDetails();
        binding.offer.setText(R.string.minerlic_bonus_bounty_m101);
        binding.features.setText(R.string.minerlic_description_m101);
    }

    private void updateDetailsT100() {
        updateDetails();
        binding.offer.setText(R.string.minerlic_bonus_bounty_t100);
        binding.features.setText(R.string.minerlic_description_t100);
    }

    private void updateDetailsT200() {
        updateDetails();
        binding.offer.setText(R.string.minerlic_bonus_bounty_t200);
        binding.features.setText(R.string.minerlic_description_t200);
    }

    private void updateDetailsT300() {
        updateDetails();
        binding.offer.setText(R.string.minerlic_bonus_bounty_t300);
        binding.features.setText(R.string.minerlic_description_t300);
    }

    public void onApply(View view) {
        final Activity activity = this;

        Tier tierLevel = viewModel.getSelectedPlan();
        if (tierLevel != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            orderRepository.createOrder(tierLevel.getId(), new OnCompleteListener<Order>() {
                @Override
                public void onComplete(Order value, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Debug.logException(error);
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // pay the order
                    Intent intent = new Intent(activity, StripeActivity.class);
                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_ID, activity), value.getId());
                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_AMOUNT, activity), value.getAmount());
                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_NAME, activity),
                            String.format("%1$s - %2$s", tierLevel.getName(), tierLevel.getShort_description()));
                    startActivityForResult(intent, STRIPE_PAYMENT);
                }
            });
        }
    }

    private void completePayment(@Nullable String token, long orderId) {
        if (token == null) return; // perhaps not possible
        if (orderId < 0) return;

        final Activity activity = this;

        binding.progressBar.setVisibility(View.VISIBLE);
        PaymentRepository repository = new PaymentRepository();
        repository.makeStripePayment(orderId, token, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                    return;
                }

                // we can consider the registration completed at this point
                MinerRepository minerRepository = new MinerRepository();
                minerRepository.updateRegistrationStatus(Constants.RegistrationStatus.REGST_COMPL, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Void value, Throwable error) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
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
}
