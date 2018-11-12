package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import org.shakticoin.databinding.FragmentRecoverySentBinding;
import org.shakticoin.util.Validator;


public class RecoverySentFragment extends Fragment {
    private RecoveryPasswordModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(RecoveryPasswordModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecoverySentBinding binding = FragmentRecoverySentBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setLifecycleOwner(this);
        if (viewModel != null) {
            binding.setViewModel(viewModel);
        }

        binding.emailAddress.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onSend();
                return true;
            }
            return false;
        });

        binding.emailAddressLayout.setValidator((view, value) -> Validator.isEmail(value));

        // display an error callout if activity is set an error message
        viewModel.emailAddressErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.emailAddressLayout.setError(s);
                viewModel.emailAddressErrMsg.setValue(null);
            }
        });

        return v;
    }

    private void onSend() {
        RecoveryPasswordActivity activity = (RecoveryPasswordActivity) getActivity();
        if (activity != null) {
            activity.resetPassword();
        }
    }
}
