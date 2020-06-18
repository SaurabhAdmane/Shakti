package com.shakticoin.app.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.databinding.FragmentProfileAdditionalPage1Binding;
import com.shakticoin.app.util.Validator;

import java.util.Objects;

public class AdditionalInfoFragment1 extends Fragment {
    public static final String TAG = AdditionalInfoFragment1.class.getSimpleName();

    FragmentProfileAdditionalPage1Binding binding;
    PersonalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileAdditionalPage1Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        binding.emailLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.phoneNumberLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        viewModel.emailAddressErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.emailLayout.setError(s);
                viewModel.emailAddressErrMsg.setValue(null);
            }
        });
        viewModel.phoneNumberErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.phoneNumberLayout.setError(s);
                viewModel.phoneNumberErrMsg.setValue(null);
            }
        });
        viewModel.occupationErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.occupationLayout.setError(s);
                viewModel.occupationErrMsg.setValue(null);
            }
        });

        return v;
    }
}
