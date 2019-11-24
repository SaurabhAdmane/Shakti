package com.shakticoin.app.payment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.databinding.FragmentPaymentOptionsPlanBinding;

import java.util.Objects;

public class PaymentOptionsPlanFragment extends Fragment {
    FragmentPaymentOptionsPlanBinding binding;
    PaymentOptionsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PaymentOptionsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionsPlanBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }

    public static PaymentOptionsPlanFragment getInstance(String planId) {
        PaymentOptionsPlanFragment fragment = new PaymentOptionsPlanFragment();
        Bundle args = new Bundle();
        args.putString("planId", planId);
        fragment.setArguments(args);
        return fragment;
    }
}
