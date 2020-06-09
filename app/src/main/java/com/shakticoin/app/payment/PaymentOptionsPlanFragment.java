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

import java.util.ArrayList;
import java.util.Objects;

public class PaymentOptionsPlanFragment extends Fragment {
    FragmentPaymentOptionsPlanBinding binding;
    PaymentOptionsViewModel viewModel;
    OptionsPageViewModel pageViewModel;
//    private VaultRepository vaultRepository;
    private ProgressBar progressBar;
//    private int vaultId = -1;
//    private PackageExtended packageExtended;
//    private PaymentOptionsViewModel.PackageType packageType;
    private String selectedLicenseTypeId;
    private String plan;
    private ArrayList<LicenseType> licenseTypes;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        progressBar = ((Activity) context).findViewById(R.id.progressBar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PaymentOptionsViewModel.class);
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
            plan = args.getString("plan");
            licenseTypes = args.getParcelableArrayList("licenseTypes");
        }

        if (licenseTypes != null && licenseTypes.size() > 0) {
            binding.planName.setText(licenseTypes.get(0).getLicName());
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

//        if (packageExtended != null) {
//            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
//            vaultRepository.getPackagePlans(vaultId, packageExtended.getId(), new OnCompleteListener<List<PackagePlanExtended>>() {
//                @Override
//                public void onComplete(List<PackagePlanExtended> plans, Throwable error) {
//                    if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
//                    if (error != null) {
//                        if (error instanceof UnauthorizedException) {
//                            startActivity(Session.unauthorizedIntent(getContext()));
//                        }
//                        return;
//                    }
//
//                    for (PackagePlanExtended plan : plans) {
//                        PackageDiscount discount = null;
//                        List<PackageDiscount> discounts = plan.getDiscount();
//                        if (discounts != null && discounts.size() > 0) {
//                            discount = discounts.get(0);
//                        }
//
//                        switch (plan.getPeriod()) {
//                            case PackagePlanExtended.WEEKLY:
//                                viewModel.weeklyPlan.set(plan);
//                                break;
//                            case PackagePlanExtended.MONTHLY:
//                                viewModel.monthlyPlan.set(plan);
////                                if (discount != null) {
////                                    binding.monthlyDiscountText.setText(discount.getDescription());
////                                }
//                                break;
//                            case PackagePlanExtended.ANNUAL:
//                                viewModel.annualPlan.set(plan);
//                                viewModel.selectedPlan.set(plan);
////                                if (discount != null) {
////                                    binding.annualDiscountText.setText(discount.getDescription());
////                                }
//                                break;
//                        }
//                    }
//                }
//            });
//        }

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

    public static PaymentOptionsPlanFragment getInstance(String selectedLicenseTypeId, String plan, ArrayList<LicenseType> licenseTypes) {
        PaymentOptionsPlanFragment fragment = new PaymentOptionsPlanFragment();
        Bundle args = new Bundle();
//        args.putString("packageType", packageTypeId);
//        args.putParcelable("package", packageExtended);
//        args.putInt("", vaultId);
        args.putString("selectedLicenseTypeId", selectedLicenseTypeId);
        args.putParcelableArrayList("licenseTypes", licenseTypes);
        args.putString("plan", plan);
        fragment.setArguments(args);
        return fragment;
    }
}
