package com.shakticoin.app.registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
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

        binding.contactMechSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewModel.isPhoneNumberChecked.set(true);
                    binding.emailAddressLayout.setVisibility(View.GONE);
                    binding.phoneNumberLayout.setVisibility(View.VISIBLE);
                    binding.phoneLabel.setTextColor(getResources().getColor(android.R.color.white));
                    binding.emailLabel.setTextColor(getResources().getColor(R.color.colorAppGrey));
                } else {
                    viewModel.isPhoneNumberChecked.set(false);
                    binding.emailAddressLayout.setVisibility(View.VISIBLE);
                    binding.phoneNumberLayout.setVisibility(View.GONE);
                    binding.emailLabel.setTextColor(getResources().getColor(android.R.color.white));
                    binding.phoneLabel.setTextColor(getResources().getColor(R.color.colorAppGrey));
                }
            }
        });
        TextView.OnEditorActionListener listener = (TextView.OnEditorActionListener) getActivity();
        if (listener != null) {
            binding.phoneNumber.setOnEditorActionListener(listener);
            binding.emailAddress.setOnEditorActionListener(listener);
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
