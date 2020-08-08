package com.shakticoin.app.referral;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.FragmentMyRefsSummaryBinding;

public class MyReferralsSummaryFragment extends Fragment {
    private FragmentMyRefsSummaryBinding binding;
    private MyReferralsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRefsSummaryBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(requireActivity()).get(MyReferralsViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        viewModel.getRemainingMonths().observe(getViewLifecycleOwner(), value ->
                binding.textWaitToUnlock.setText(Html.fromHtml(getString(R.string.my_refs_tounlock, value))));

        return v;
    }
}
