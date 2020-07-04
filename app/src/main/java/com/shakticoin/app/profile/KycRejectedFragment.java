package com.shakticoin.app.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shakticoin.app.databinding.FragmentKycRejectedBinding;

public class KycRejectedFragment extends Fragment {
    private FragmentKycRejectedBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycRejectedBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        binding.doOpenProfile.setOnClickListener(v -> openProfile());

        return binding.getRoot();
    }

    private void openProfile() {
        final Activity activity = getActivity();
        if (activity != null) {
            startActivity(new Intent(activity, ProfileActivity.class));
            activity.finish();
        }
    }
}
