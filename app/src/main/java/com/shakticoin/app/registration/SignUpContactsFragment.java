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

import com.shakticoin.app.databinding.FragmentSignupContactBinding;
import com.shakticoin.app.util.Validator;


public class SignUpContactsFragment extends Fragment {
    private SignUpActivityModel viewModel;
    private FragmentSignupContactBinding binding;

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
        binding = FragmentSignupContactBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        if (viewModel != null) {
            binding.setViewModel(viewModel);
        }

        TextView.OnEditorActionListener listener = (TextView.OnEditorActionListener) getActivity();
        if (listener != null) {
            binding.phoneNumber.setOnEditorActionListener(listener);
        }

        // display a special icon if content of the field conform the target format
        binding.emailAddressLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.phoneNumberLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        // display error callout for the field if error message is set
        viewModel.firstNameErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.firstNameLayout.setError(s);
                viewModel.firstNameErrMsg.setValue(null);
            }
        });
        viewModel.lastNameErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.lastNameLayout.setError(s);
                viewModel.lastNameErrMsg.setValue(null);
            }
        });
        viewModel.emailAddressErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.emailAddressLayout.setError(s);
                viewModel.emailAddressErrMsg.setValue(null);
            }
        });
        viewModel.phoneNumberErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.phoneNumberLayout.setError(s);
                viewModel.phoneNumberErrMsg.setValue(null);
            }
        });

        return binding.getRoot();
    }
}