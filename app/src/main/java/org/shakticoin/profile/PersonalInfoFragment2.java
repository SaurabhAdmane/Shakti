package org.shakticoin.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shakticoin.databinding.FragmentProfilePersonalPage2Binding;

import java.util.Objects;

public class PersonalInfoFragment2 extends Fragment {
    public static final String TAG = PersonalInfoFragment2.class.getSimpleName();

    private FragmentProfilePersonalPage2Binding binding;
    private PersonalInfoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilePersonalPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }
}