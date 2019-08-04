package org.shakticoin.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.R;
import org.shakticoin.databinding.FragmentWalletHomeBinding;

public class HomeFragment extends Fragment {
    private WalletModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(WalletModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWalletHomeBinding binding = FragmentWalletHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        if (viewModel != null) binding.setViewModel(viewModel);

        binding.onShareCode.setOnClickListener(v -> shareReferralCode());
        binding.onShareLink.setOnClickListener(v -> shareReferralUrl());

        return binding.getRoot();
    }

    private void shareReferralUrl() {
        String referralUrl = viewModel.referralLink.get();
        if (!TextUtils.isEmpty(referralUrl)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, referralUrl);
            startActivity(Intent.createChooser(intent, getString(R.string.share_referral_link)));
        }
    }

    private void shareReferralCode() {
        String referralCode = viewModel.referralCode.get();
        if (!TextUtils.isEmpty(referralCode)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, referralCode);
            startActivity(Intent.createChooser(intent, getString(R.string.share_referral_code)));
        }
    }
}
