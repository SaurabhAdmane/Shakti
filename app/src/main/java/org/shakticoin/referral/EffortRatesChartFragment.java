package org.shakticoin.referral;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shakticoin.databinding.FragmentEffortChartBinding;

public class EffortRatesChartFragment extends Fragment {
    private FragmentEffortChartBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEffortChartBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        return v;
    }
}
