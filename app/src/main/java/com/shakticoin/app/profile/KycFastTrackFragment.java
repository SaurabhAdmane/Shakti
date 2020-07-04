package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.databinding.FragmentKycFasttrackBinding;

import java.util.Objects;

public class KycFastTrackFragment extends Fragment {
    private FragmentKycFasttrackBinding binding;
    private PersonalViewModel viewModel;
    private KYCRepository kycRepository = new KYCRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycFasttrackBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalViewModel.class);
        binding.setViewModel(viewModel);

        binding.doPayFastVerification.setOnClickListener(v -> payFastTrack());

        return binding.getRoot();
    }

    private void payFastTrack() {
        viewModel.getProgressBarTrigger().set(true);
        kycRepository.subscription(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                viewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(ShaktiApplication.getContext()));
                    } else {
                        Toast.makeText(ShaktiApplication.getContext(), R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                getActivity().finish();
            }
        });
    }
}
