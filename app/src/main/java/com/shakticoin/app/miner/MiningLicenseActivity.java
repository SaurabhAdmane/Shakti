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
import com.shakticoin.app.api.license.LicenceRepository;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.ActivityMiningLicenseBinding;
import com.shakticoin.app.payment.PaymentOptionsActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.DrawerActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MiningLicenseActivity extends DrawerActivity {

    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;
    private VaultRepository vaultRepository = new VaultRepository();
    private LicenseRepository licenseRepository = new LicenseRepository();

    private int vaultId = -1;

    private ArrayList<LicenseType> licenseTypesAll;

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

        final AppCompatActivity self = this;

//        if (savedInstanceState != null) {
//        } else {
//        }
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
                    if (LicenceRepository.getMINING_PLANS().contains(t.getPlanCode())
                            && Integer.valueOf(1).equals(t.getCycle())) {
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
                binding.onClaim.setText(getString(R.string.minerlic_action, packageTypeName));
            }

            String bonusDescription = licenseType.getBonus();
            if (bonusDescription != null) {
                binding.offer.setText(bonusDescription);
            }

            Double price = licenseType.getPrice();
            if (price != null) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                binding.paymentAmount.setText(formatter.format(price));
            }

            binding.onClaim.setEnabled(true);
        }
    }

    public void onApply(View view) {
        Intent intent = new Intent(this, PaymentOptionsActivity.class);
//        intent.putExtra(CommonUtil.prefixed("vaultId", this), vaultId);
        LicenseType licenseType = viewModel.getSelectedPackage();
        intent.putExtra(CommonUtil.prefixed("licenseTypeId", this), licenseType.getId());
        intent.putParcelableArrayListExtra(CommonUtil.prefixed("licenses", this), licenseTypesAll);
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