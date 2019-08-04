package org.shakticoin.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.databinding.FragmentProfileKycSelectorBinding;

public class KycSelectorFragment extends Fragment {
    public static final String TAG = KycSelectorFragment.class.getSimpleName();

    private FragmentProfileKycSelectorBinding binding;
    private KycSelectorViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileKycSelectorBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycSelectorViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        binding.doNext.setOnClickListener(v1 -> onNext());
        return v;
    }

    private void onNext() {
        Intent intent = new Intent(getActivity(), KycActivity.class);
        startActivity(intent);
    }

}
