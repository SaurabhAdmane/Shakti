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

import com.shakticoin.app.databinding.FragmentProfilePersonalPage1Binding;

import java.util.Objects;

public class PersonalInfoFragment1 extends Fragment {
    public static final String TAG = PersonalInfoFragment1.class.getSimpleName();

    private FragmentProfilePersonalPage1Binding binding;
    private PersonalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilePersonalPage1Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        viewModel.firstNameErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.firstNameLayout.setError(s);
                viewModel.lastNameErrMsg.setValue(null);
            }
        });
        viewModel.lastNameErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.lastNameLayout.setError(s);
                viewModel.lastNameErrMsg.setValue(null);
            }
        });

        return v;
    }
}
