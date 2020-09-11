package com.shakticoin.app.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shakticoin.app.databinding.FragmentInlineWalletKycVerificationBinding;
import com.shakticoin.app.profile.ProfileActivity;

public class KycVerificationRequiredFragment extends Fragment {
    FragmentInlineWalletKycVerificationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInlineWalletKycVerificationBinding.inflate(getLayoutInflater(), container, false);
        binding.setLifecycleOwner(this);

        /** TODO: temporarily disabled for QA
        binding.doVerification.setOnClickListener(this::onKycVerification);
        */

        return binding.getRoot();
    }

    private void onKycVerification(View v) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }
}
