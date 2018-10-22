package org.shakticoin.registration;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityMiningLicenseBinding;
import org.shakticoin.widget.CheckableRoundButton;

import java.math.BigDecimal;

public class MiningLicenseActivity extends AppCompatActivity {
    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;

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

        viewModel.selectedPlan.observe(this, new Observer<MiningLicenseModel.Plan>() {
            @Override
            public void onChanged(@Nullable MiningLicenseModel.Plan plan) {
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
                    case T400:
                        updateDetailsT400();
                        break;
                }
            }
        });
    }

    private void updateDetails() {
        String planName = viewModel.selectedPlan.getValue().name();
        binding.license.setText(getString(R.string.minerlic_license, planName));
        binding.onClaim.setText(getString(R.string.minerlic_action, planName));

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

    private void updateDetailsT400() {
        updateDetails();
        binding.offer.setText(R.string.minerlic_bonus_bounty_t400);
        binding.features.setText(R.string.minerlic_description_t400);
    }

    @BindingAdapter("is_checked")
    public static void setButtonState(CheckableRoundButton view, Boolean value) {
        view.setChecked(value);
    }

    @InverseBindingAdapter(attribute = "is_checked", event = "app:is_checkedAttrChanged")
    public static Boolean getButtonState(CheckableRoundButton view) {
        return view.isChecked();
    }

    @BindingAdapter("app:is_checkedAttrChanged")
    public static void setListeners(CheckableRoundButton view, final InverseBindingListener attrChange) {
        view.setOnCheckedChangeListener((buttonView, isChecked) -> attrChange.onChange());
    }
}
