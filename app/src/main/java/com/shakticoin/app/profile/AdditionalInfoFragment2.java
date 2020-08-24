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

public class AdditionalInfoFragment2 extends Fragment {
    public static final String TAG = AdditionalInfoFragment2.class.getSimpleName();

    private FragmentProfileAdditionalPage2Binding binding;
    private PersonalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileAdditionalPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(requireActivity()).get(PersonalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        viewModel.kinContactErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.phoneOrEmailLayout.setError(s);
                viewModel.kinContactErrMsg.setValue(null);
            }
        });

        return v;
    }
}
