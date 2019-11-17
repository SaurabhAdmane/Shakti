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

import com.shakticoin.app.databinding.FragmentProfileAdditionalPage2Binding;
import com.shakticoin.app.util.Validator;

import java.util.Objects;

public class AdditionalInfoFragment2 extends Fragment {
    public static final String TAG = AdditionalInfoFragment2.class.getSimpleName();

    private FragmentProfileAdditionalPage2Binding binding;
    private AdditionalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileAdditionalPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AdditionalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        binding.phoneOrEmailLayout.setValidator((view, value) -> Validator.isEmailOrPhoneNumber(value));

        viewModel.kinFullNameErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.fullNameLayout.setError(s);
                viewModel.kinFullNameErrMsg.setValue(null);
            }
        });
        viewModel.kinContactErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.phoneOrEmailLayout.setError(s);
                viewModel.kinContactErrMsg.setValue(null);
            }
        });
        viewModel.kinRelationshipErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.relationshipLayout.setError(s);
                viewModel.kinRelationshipErrMsg.setValue(null);
            }
        });

        return v;
    }
}
