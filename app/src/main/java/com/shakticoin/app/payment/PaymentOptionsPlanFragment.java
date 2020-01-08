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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.vault.PackageDiscount;
import com.shakticoin.app.api.vault.PackageExtended;
import com.shakticoin.app.api.vault.PackagePlanExtended;
import com.shakticoin.app.api.vault.VaultRepository;
import com.shakticoin.app.databinding.FragmentPaymentOptionsPlanBinding;
import com.shakticoin.app.util.Debug;

import java.util.List;
import java.util.Objects;

public class PaymentOptionsPlanFragment extends Fragment {
    FragmentPaymentOptionsPlanBinding binding;
    PaymentOptionsViewModel viewModel;
    private VaultRepository vaultRepository;
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        progressBar = ((Activity) context).findViewById(R.id.progressBar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PaymentOptionsViewModel.class);
        vaultRepository = new VaultRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionsPlanBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        Bundle args = getArguments();
        PaymentOptionsViewModel.PackageType packageType = PaymentOptionsViewModel.PackageType.valueOf(args.getString("packageType"));
        PackageExtended packageExtended = args.getParcelable("package");
        if (packageExtended != null) {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            vaultRepository.getPackagePlans(2, packageExtended.getId(), new OnCompleteListener<List<PackagePlanExtended>>() {
                @Override
                public void onComplete(List<PackagePlanExtended> plans, Throwable error) {
                    if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        if (error instanceof UnauthorizedException) {
                            startActivity(Session.unauthorizedIntent(getContext()));
                        }
                        return;
                    }

                    for (PackagePlanExtended plan : plans) {
                        PackageDiscount discount = null;
                        List<PackageDiscount> discounts = plan.getDiscount();
                        if (discounts != null && discounts.size() > 0) {
                            discount = discounts.get(0);
                        }

                        switch (plan.getPeriod()) {
                            case PackagePlanExtended.WEEKLY:
                                viewModel.weeklyPlan.set(plan);
                                break;
                            case PackagePlanExtended.MONTHLY:
                                viewModel.monthlyPlan.set(plan);
//                                if (discount != null) {
//                                    binding.monthlyDiscountText.setText(discount.getDescription());
//                                }
                                break;
                            case PackagePlanExtended.ANNUAL:
                                viewModel.annualPlan.set(plan);
//                                if (discount != null) {
//                                    binding.annualDiscountText.setText(discount.getDescription());
//                                }
                                break;
                        }
                    }
                }
            });
        }

        return v;
    }

    @Override
    public void onDetach() {
        progressBar = null;
        super.onDetach();
    }

    public static PaymentOptionsPlanFragment getInstance(String packageTypeId, PackageExtended packageExtended) {
        PaymentOptionsPlanFragment fragment = new PaymentOptionsPlanFragment();
        Bundle args = new Bundle();
        args.putString("packageType", packageTypeId);
        args.putParcelable("package", packageExtended);
        fragment.setArguments(args);
        return fragment;
    }
}
