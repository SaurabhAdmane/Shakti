package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.order.Order;
import org.shakticoin.api.order.OrderRepository;
import org.shakticoin.databinding.ActivityMiningLicenseBinding;
import org.shakticoin.util.Debug;
import org.shakticoin.widget.CheckableRoundButton;

import java.math.BigDecimal;

public class MiningLicenseActivity extends AppCompatActivity {
    private ActivityMiningLicenseBinding binding;
    private MiningLicenseModel viewModel;
    private OrderRepository orderRepository;

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
                case T400:
                    updateDetailsT400();
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

    public void onApply(View view) {
        final Activity activity = this;

        Long tierLevel = viewModel.getSelectedPlan();
        if (tierLevel != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            orderRepository.createOrder(tierLevel, new OnCompleteListener<Order>() {
                @Override
                public void onComplete(Order value, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Debug.logException(error);
                        Toast.makeText(activity, R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // pay the order
                }
            });
        }
    }
}
