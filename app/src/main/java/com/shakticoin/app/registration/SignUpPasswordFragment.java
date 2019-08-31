package com.shakticoin.app.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shakticoin.app.databinding.FragmentSignupPasswordBinding;

public class SignUpPasswordFragment extends Fragment {
    private SignUpActivityModel viewModel;
    private FragmentSignupPasswordBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(SignUpActivityModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupPasswordBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        if (viewModel != null) {
            binding.setViewModel(viewModel);
        }

        TextView.OnEditorActionListener listener = (TextView.OnEditorActionListener) getActivity();
        if (listener != null) {
            binding.confirmPassword.setOnEditorActionListener(listener);
        }

        // display error callout for the field if error message is set
        viewModel.newPasswordErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.newPasswordLayout.setError(s);
                viewModel.newPasswordErrMsg.setValue(null);
            }
        });
        viewModel.verifyPasswordErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.confirmPasswordLayout.setError(s);
                viewModel.verifyPasswordErrMsg.setValue(null);
            }
        });

        return binding.getRoot();
    }
}
