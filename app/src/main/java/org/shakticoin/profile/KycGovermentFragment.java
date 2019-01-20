package org.shakticoin.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shakticoin.databinding.FragmentKycGovermentBinding;

public class KycGovermentFragment extends Fragment {
    private FragmentKycGovermentBinding binding;
    private KycGovermentViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycGovermentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycGovermentViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }
}
