package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shakticoin.app.databinding.FragmentCompanyAdditionalPage2Binding;

public class CompanyAdditionalInfoFragment2 extends Fragment {
    private FragmentCompanyAdditionalPage2Binding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompanyAdditionalPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        return v;
    }
}
