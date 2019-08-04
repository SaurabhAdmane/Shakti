package org.shakticoin.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
