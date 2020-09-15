package com.shakticoin.app.miner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.api.license.MiningLicenseCycle;
import com.shakticoin.app.api.license.ModelsKt;
import com.shakticoin.app.api.license.NodeOperatorModel;
import com.shakticoin.app.api.license.SubscribedLicenseModel;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityMiningLicenseBinding;
import com.shakticoin.app.payment.PaymentOptionsActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DrawerActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static com.shakticoin.app.api.license.LicenseTypeKt.compareLicenseType;

public class MiningLicenseActivity extends DrawerActivity {

    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;
    private VaultRepository vaultRepository;
    private LicenseRepository licenseRepository;

    private int vaultId = -1;

    private ArrayList<LicenseType> licenseTypesAll;

    private SubscribedLicenseModel currentSubscription;
    private int possibleAction = PaymentOptionsActivity.LIC_ACTION_APPLY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mining_license);
        final ViewGroup root = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        binding = ActivityMiningLicenseBinding.bind(root);
        binding.setLifecycleOwner(this);

        // need to call in any subclass of DrawerActivity
        super.onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        viewModel = ViewModelProviders.of(this).get(MiningLicenseModel.class);
        binding.setViewModel(viewModel);

        licenseRepository = new LicenseRepository();
        licenseRepository.setLifecycleOwner(this);
        vaultRepository = new VaultRepository();
        vaultRepository.setLifecycleOwner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final AppCompatActivity activity = this;
        binding.progressBar.setVisibility(View.VISIBLE);
        licenseRepository.getLicenses(new OnCompleteListener<List<LicenseType>>() {
            @Override
            public void onComplete(List<LicenseType> licenseTypes, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(ShaktiApplication.getContext(), Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                    return;
                }

                // keep for further
                licenseTypesAll = new ArrayList<>();
                licenseTypesAll.addAll(licenseTypes);

                // we need only year mining licenses in this activity
                List<LicenseType> yearMiningLics = new ArrayList<>();
                for (LicenseType t : licenseTypes) {
                    if (ModelsKt.getMINING_PLANS().contains(t.getPlanType())
                            && MiningLicenseCycle.Y.name().equals(t.getModeType())) {
                        yearMiningLics.add(t);
                    }
                }

                viewModel.init(yearMiningLics);
                viewModel.selectedPlan.observe(activity, type -> {
                    if (type == null) return;
                    LicenseType licenseType = viewModel.getSelectedPackage();
                    if (licenseType != null) {
                        updateDetails(licenseType);
                    }
                });

            }
        });

        // retrieve information about node operator and licenses he might bought
        licenseRepository.getNodeOperator(new OnCompleteListener<NodeOperatorModel>() {
            @Override
            public void onComplete(NodeOperatorModel value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }

                List<SubscribedLicenseModel> subscriptions = value.getSubscribedLicenses();
                if (subscriptions != null) {
                    currentSubscription = CommonUtil.getActiveSubscription(subscriptions);
                    updateDetails(viewModel.getSelectedPackage());
                }
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void updateDetails(LicenseType licenseType) {
        if (licenseType != null) {
            MiningLicenseModel.LicenseTypeIds type = viewModel.selectedPlan.getValue();
            binding.features.setText(HtmlCompat.fromHtml(
                    formatFeatureList(type, licenseType), HtmlCompat.FROM_HTML_MODE_LEGACY));

            MiningLicenseModel.LicenseTypeIds packageTypeName = viewModel.selectedPlan.getValue();
            if (packageTypeName != null) {
                binding.license.setText(licenseType.getLicName());
            }

            String bonusDescription = licenseType.getBonus();
            if (bonusDescription != null) {
                binding.offer.setText(bonusDescription);
            }

            Double price = licenseType.getPrice();
            if (price != null) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                formatter.setCurrency(Currency.getInstance("USD"));
                binding.paymentAmount.setText(formatter.format(price));
            }

            if (currentSubscription != null) {
                int comparisionResult = compareLicenseType(licenseType.getPlanType(), currentSubscription.getPlanType());
                if (comparisionResult == 0) {
                    binding.doAction.setEnabled(false);
                    binding.doAction.setText(getString(R.string.minerlic_action_purchased));
                    possibleAction = PaymentOptionsActivity.LIC_ACTION_NONE;
                } else if (comparisionResult > 0) {
                    binding.doAction.setEnabled(true);
                    binding.doAction.setText(getString(R.string.minerlic_action_upgrade, licenseType.getPlanType()));
                    possibleAction = PaymentOptionsActivity.LIC_ACTION_UPGRADE;
                } else {
                    binding.doAction.setEnabled(true);
                    binding.doAction.setText(getString(R.string.minerlic_action_downgrade, licenseType.getPlanType()));
                    possibleAction = PaymentOptionsActivity.LIC_ACTION_DOWNGRADE;
                }
            } else {
                binding.doAction.setEnabled(true);
                binding.doAction.setText(getString(R.string.minerlic_action, licenseType.getPlanType()));
                possibleAction = PaymentOptionsActivity.LIC_ACTION_APPLY;
            }
        }
    }

    public void onApply(View view) {
        Intent intent = new Intent(this, PaymentOptionsActivity.class);
        LicenseType licenseType = viewModel.getSelectedPackage();
        intent.putExtra(CommonUtil.prefixed("licenseTypeId"), licenseType.getId());
        if (currentSubscription != null) {
            intent.putExtra(CommonUtil.prefixed("selectedPlanType"), currentSubscription.getPlanType());
        }
        intent.putParcelableArrayListExtra(CommonUtil.prefixed("licenses"), licenseTypesAll);
        startActivity(intent);
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
    private String formatFeatureList(MiningLicenseModel.LicenseTypeIds packageType, LicenseType licenseType) {
        List<String> features = licenseType.getLicFeatures();
        StringBuilder sb = new StringBuilder();
        sb.append("<p><b>");
        if (packageType != null) {
            sb.append(getString(R.string.minerlic_package_features, packageType.name()));
        } else {
            sb.append(getString(R.string.minerlic_features));
        }
        sb.append("</b></p>");
        if (features != null && features.size() > 0) {
            for (String feature : features) {
                sb.append("<p>").append("\u2022 ").append(feature).append("</p>");
            }
        }
        return sb.toString();
    }
}
