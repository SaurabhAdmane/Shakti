package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.databinding.FragmentKycUtilityBinding;

public class KycUtilityFragment extends Fragment {
    private FragmentKycUtilityBinding binding;
    private KycUtilityViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycUtilityBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycUtilityViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }
}
