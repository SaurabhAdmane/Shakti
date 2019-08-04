package org.shakticoin.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.databinding.FragmentKycPropertyBinding;

public class KycPropertyFragment extends Fragment {
    private FragmentKycPropertyBinding binding;
    private KycPropertyViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycPropertyBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycPropertyViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        return v;
    }
}
