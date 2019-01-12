package org.shakticoin.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shakticoin.databinding.FragmentProfileAdditionalPage1Binding;

import java.util.Objects;

public class AdditionalInfoPage1 extends Fragment {
    public static final String TAG = AdditionalInfoPage1.class.getSimpleName();

    FragmentProfileAdditionalPage1Binding binding;
    AdditionalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileAdditionalPage1Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AdditionalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }
}
