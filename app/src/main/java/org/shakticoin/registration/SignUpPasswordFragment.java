package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.shakticoin.databinding.FragmentSignupPasswordBinding;

public class SignUpPasswordFragment extends Fragment {
    private SignUpActivityModel viewModel;

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
        FragmentSignupPasswordBinding binding = FragmentSignupPasswordBinding.inflate(inflater, container, false);
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
