package org.shakticoin.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shakticoin.databinding.FragmentKycFilesBinding;

public class KycFilesFragment extends Fragment {
    private FragmentKycFilesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycFilesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        return v;
    }
}
