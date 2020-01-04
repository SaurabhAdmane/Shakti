package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.order.OrderRepository;
import com.shakticoin.app.api.payment.PaymentRepository;
import com.shakticoin.app.api.tier.Tier;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.api.vault.Bonus;
import com.shakticoin.app.api.vault.PackageExtended;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityMiningLicenseBinding;
import com.shakticoin.app.payment.PaymentOptionsActivity;
import com.shakticoin.app.payment.stripe.StripeActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;

import java.math.BigDecimal;
import java.util.List;

public class MiningLicenseActivity extends AppCompatActivity {
    public static final int STRIPE_PAYMENT  = 100;

    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;
    private OrderRepository orderRepository;
    private VaultRepository vaultRepository;

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

        AppCompatActivity self = this;

        vaultRepository = new VaultRepository();
        binding.progressBar.setVisibility(View.VISIBLE);
        // FIXME: vault ID should not be a constant here
        vaultRepository.getVaultPackages(2, new OnCompleteListener<List<PackageExtended>>() {
            @Override
            public void onComplete(List<PackageExtended> packages, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    return;
                }

                viewModel.init(packages);
                viewModel.selectedPlan.observe(self, packageType -> {
                    if (packageType == null) return;
                    PackageExtended packageExtended = viewModel.getSelectedPackage();
                    if (packageExtended != null) {
                        updateDetails(packageExtended);
                    }
                });
            }
        });

        orderRepository = new OrderRepository();
    }

    private void updateDetails(PackageExtended packageExtended) {
        if (packageExtended != null) {
            MiningLicenseModel.PackageType packageType = viewModel.selectedPlan.getValue();
            binding.features.setText(HtmlCompat.fromHtml(
                    formatFeatureList(packageType, packageExtended), HtmlCompat.FROM_HTML_MODE_LEGACY));

            MiningLicenseModel.PackageType packageTypeName = viewModel.selectedPlan.getValue();
            if (packageTypeName != null) {
                binding.license.setText(packageExtended.getName());
                binding.onClaim.setText(getString(R.string.minerlic_action, packageTypeName));
            }

            Bonus bonus = packageExtended.getBonus();
            if (bonus != null) {
                binding.offer.setText(bonus.getDescription());
                BigDecimal price = viewModel.getPaymentAmount();
                binding.paymentAmount.setText(price != null ?
                        String.format(getResources().getConfiguration().locale, "$%1$.2f", price) : "");
            }
        }
    }

    public void onApply(View view) {
        Intent intent = new Intent(this, PaymentOptionsActivity.class);
        Tier tierLevel = viewModel.getSelectedPlan();
        if (intent != null) {
            intent.putExtra("PLAN_ID", tierLevel.getId());
        }
        startActivity(intent);
//        final Activity activity = this;
//
//        Tier tierLevel = viewModel.getSelectedPlan();
//        if (tierLevel != null) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            orderRepository.createOrder(tierLevel.getId(), new OnCompleteListener<Order>() {
//                @Override
//                public void onComplete(Order value, Throwable error) {
//                    binding.progressBar.setVisibility(View.INVISIBLE);
//                    if (error != null) {
//                        Debug.logException(error);
//                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // pay the order
//                    Intent intent = new Intent(activity, StripeActivity.class);
//                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_ID, activity), value.getId());
//                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_AMOUNT, activity), value.getAmount());
//                    intent.putExtra(CommonUtil.prefixed(StripeActivity.KEY_ORDER_NAME, activity),
//                            String.format("%1$s - %2$s", tierLevel.getName(), tierLevel.getShort_description()));
//                    startActivityForResult(intent, STRIPE_PAYMENT);
//                }
//            });
//        }
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
                UserRepository userRepository = new UserRepository();
                userRepository.updateRegistrationStatus(Constants.RegistrationStatus.REGST_COMPL, new OnCompleteListener<Void>() {
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

    /**
     * Format bullet list string from a plain list of features.
     */
    private String formatFeatureList(MiningLicenseModel.PackageType packageType, PackageExtended packageExtended) {
        List<String> features = packageExtended.getFeatures();
        StringBuilder sb = new StringBuilder();
        if (packageType != null) {
            sb.append("<p><b>").append(getString(R.string.minerlic_package_features, packageType.name())).append("</b></p>");
        } else {
            sb.append(getString(R.string.minerlic_features));
        }
        if (features != null && features.size() > 0) {
            for (String feature : features) {
                sb.append("<p>").append("\u2022 ").append(feature).append("</p>");
            }
        }
        return sb.toString();
    }
}
