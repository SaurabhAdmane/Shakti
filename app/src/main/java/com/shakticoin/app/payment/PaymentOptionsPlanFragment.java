package com.shakticoin.app.payment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.databinding.FragmentPaymentOptionsPlanBinding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentOptionsPlanFragment extends Fragment {
    FragmentPaymentOptionsPlanBinding binding;
    PaymentOptionsViewModel viewModel;
    OptionsPageViewModel pageViewModel;
    private ProgressBar progressBar;
    private String selectedLicenseTypeId;
    private String plan;
    private String existingPlanType;
    private LicenseType selectedLicenseType;

    private Integer maxMinCircle = 0;
    private Map<OptionsPageViewModel.Period, Integer> discounts = new HashMap<>(3);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        progressBar = ((Activity) context).findViewById(R.id.progressBar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(PaymentOptionsViewModel.class);
        pageViewModel = ViewModelProviders.of(this).get(OptionsPageViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionsPlanBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setPageViewModel(pageViewModel);
        View v = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            selectedLicenseTypeId = args.getString("selectedLicenseTypeId");
            existingPlanType = args.getString("existingPlanType");
            plan = args.getString("plan");
            pageViewModel.licenseTypes = args.getParcelableArrayList("licenseTypes");
            if (pageViewModel.licenseTypes != null) {
                Collections.sort(pageViewModel.licenseTypes, (o1, o2) ->
                        Objects.requireNonNull(o1.getCycle()).compareTo(Objects.requireNonNull(o2.getCycle()))*-1);
            }
        }

        if (pageViewModel.licenseTypes != null && pageViewModel.licenseTypes.size() > 0) {
            binding.planName.setText(pageViewModel.licenseTypes.get(0).getLicName());
        }

        PaymentOptionsViewModel.PackageType[] packageTypes = PaymentOptionsViewModel.PackageType.values();
        if (plan != null) {
            if (plan.equals(packageTypes[0].name())) {
                // the fragment represents first plan and arrow "prev" must be disabled
                binding.doGoLeft.setVisibility(View.INVISIBLE);
            } else if (plan.equals(packageTypes[packageTypes.length-1].name())) {
                // the fragment represents last plan and arrow "next" must be disabled
                binding.doGoRight.setVisibility(View.INVISIBLE);
            }
        }

        // calculate discounts which user may have when select longer period
        if (pageViewModel.licenseTypes != null) {
            LicenseType shortestPlan = pageViewModel.licenseTypes.get(0);
            if (shortestPlan != null) {
                maxMinCircle = shortestPlan.getCycle();
                BigDecimal basePrice = BigDecimal.valueOf(shortestPlan.getPrice());
                for (int i = 1; i < pageViewModel.licenseTypes.size(); i++) {
                    LicenseType licenseType = pageViewModel.licenseTypes.get(i);
                    OptionsPageViewModel.Period period = null;
                    Integer cycle = licenseType.getCycle();
                    if (cycle == null) continue;
                    switch (licenseType.getCycle()) {
                        case 1:
                            period = OptionsPageViewModel.Period.ANNUAL;
                            break;
                        case 12:
                            period = OptionsPageViewModel.Period.MONTHLY;
                            break;
                    }
                    BigDecimal k = BigDecimal.valueOf(maxMinCircle)
                            .divide(BigDecimal.valueOf(licenseType.getCycle().longValue()), 1, BigDecimal.ROUND_DOWN);
                    BigDecimal relPrice = BigDecimal.valueOf(licenseType.getPrice()).divide(k, BigDecimal.ROUND_HALF_UP);
                    BigDecimal percents = BigDecimal.ONE.subtract(
                            relPrice.divide(basePrice, BigDecimal.ROUND_HALF_UP)
                    ).multiply(BigDecimal.valueOf(100));
                    discounts.put(period, percents.intValue());
                }

                Integer discount = discounts.get(OptionsPageViewModel.Period.MONTHLY);
                if (discount != null) {
                    binding.monthlyDiscountText.setText(getString(R.string.pmnt_opts_saving, discount));
                }
                discount = discounts.get(OptionsPageViewModel.Period.ANNUAL);
                if (discount != null) {
                    binding.annualDiscountText.setText(getString(R.string.pmnt_opts_saving, discount));
                }

                pageViewModel.selectedPeriod.observe(getViewLifecycleOwner(), period -> {
                    String modeType = "W";
                    int numberPeriods = maxMinCircle;

                    switch (period) {
                        case ANNUAL:
                            modeType = "Y";
                            numberPeriods = 1;
                            break;
                        case MONTHLY:
                            modeType = "M";
                            numberPeriods = 12;
                            break;
                    }

                    for (LicenseType licenseType : pageViewModel.licenseTypes) {
                        if (modeType.equals(licenseType.getModeType())) {
                            selectedLicenseType = licenseType;
                            break;
                        }
                    }

                    binding.textAmountUSD.setText(getString(R.string.pmnt_opts_amount, selectedLicenseType.getPrice()));
                    binding.doMainAction.setTag(selectedLicenseType.getPlanCode());
                    binding.textFullSaving.setText(
                            numberPeriods < maxMinCircle && discounts.get(period) != null ?
                                    getString(R.string.pmnt_opts_saving_long, discounts.get(period)) : "");

                    // if user selected an existing license then we should disable action button
                    if (selectedLicenseType.getPlanType() != null && selectedLicenseType.getPlanType().equals(existingPlanType)) {
                        binding.doMainAction.setText(R.string.minerlic_action_purchased);
                        binding.doMainAction.setEnabled(false);
                    } else {
                        binding.doMainAction.setText(R.string.pmnt_opts_main_action);
                        binding.doMainAction.setEnabled(true);
                    }
                });
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        progressBar = null;
        super.onDetach();
    }

    /**
     * Instantiate a payment options plan page.
     *
     * @param selectedLicenseTypeId License type ID.
     * @param existingPlanType This is plan type the user bought already and it is active at the time being.
     * @param plan The plan type that user has selected in the selector.
     * @param licenseTypes
     */
    public static PaymentOptionsPlanFragment getInstance(
            String selectedLicenseTypeId, String existingPlanType, String plan, ArrayList<LicenseType> licenseTypes) {
        PaymentOptionsPlanFragment fragment = new PaymentOptionsPlanFragment();
        Bundle args = new Bundle();
        args.putString("selectedLicenseTypeId", selectedLicenseTypeId);
        args.putString("existingPlanType", existingPlanType);
        args.putParcelableArrayList("licenseTypes", licenseTypes);
        args.putString("plan", plan);
        fragment.setArguments(args);
        return fragment;
    }
}
