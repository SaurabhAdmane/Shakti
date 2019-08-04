package org.shakticoin.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.R;
import org.shakticoin.databinding.FragmentWalletMainBinding;
import org.shakticoin.profile.ProfileActivity;

import java.util.Objects;

public class MainFragment extends Fragment {

    private FragmentWalletMainBinding binding;
    private WalletModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(WalletModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWalletMainBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.doAdmin.setOnClickListener(this::onAdminWallet);
        binding.doVerification.setOnClickListener(this::onVerification);
        binding.doBalanceHistory.setOnClickListener(this::onBalanceHistory);
        binding.doRefer.setOnClickListener(this::onShowUnlockInfo);
        binding.doBecomeMiner.setOnClickListener(this::onShowMinerInfo);
        binding.doLearnMore.setOnClickListener(this::onShowBusinessVaultInfo);

        return v;
    }

    private void onAdminWallet(View v) {
        Toast.makeText(getActivity(), R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    private void onVerification(View v) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    private void onBalanceHistory(View v) {
        Toast.makeText(getActivity(), R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    private void onShowBusinessVaultInfo(View v) {
        DialogBusinessVault.newInstance().show(getFragmentManager(), DialogBusinessVault.TAG);
    }

    private void onShowMinerInfo(View v) {
        DialogBecomeMiner.newInstance().show(getFragmentManager(), DialogBecomeMiner.TAG);
    }

    private void onShowUnlockInfo(View v) {
        DialogGrabWallet.newInstance().show(getFragmentManager(), DialogGrabWallet.TAG);
    }
}
