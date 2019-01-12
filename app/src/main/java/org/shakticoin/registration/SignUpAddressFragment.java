package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.shakticoin.api.country.Country;
import org.shakticoin.databinding.FragmentSignupAddressBinding;
import org.shakticoin.widget.InlineLabelSpinner;
import org.shakticoin.widget.SpinnerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SignUpAddressFragment extends Fragment {
    private SignUpActivityModel viewModel;
    private FragmentSignupAddressBinding binding;

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
        binding = FragmentSignupAddressBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        if (viewModel != null) {
            binding.setViewModel(viewModel);
        }

        TextView.OnEditorActionListener listener = (TextView.OnEditorActionListener) getActivity();
        if (listener != null) {
            binding.postalCode.setOnEditorActionListener(listener);
        }

        // display error callout for the field if error message is set
        viewModel.countryCodeErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.countriesLayout.setError(s);
                viewModel.countryCodeErrMsg.setValue(null);
            }
        });
        viewModel.citizenshipCodeErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.citizenshipLayout.setError(s);
                viewModel.citizenshipCodeErrMsg.setValue(null);
            }
        });
        viewModel.addressErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.addressLayout.setError(s);
                viewModel.addressErrMsg.setValue(null);
            }
        });
        viewModel.cityErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.cityLayout.setError(s);
                viewModel.cityErrMsg.setValue(null);
            }
        });
        viewModel.postalCodeErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.postalCodeLayout.setError(s);
                viewModel.postalCodeErrMsg.setValue(null);
            }
        });
        viewModel.stateErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.stateLayout.setError(s);
                viewModel.stateErrMsg.setValue(null);
            }
        });

        return binding.getRoot();
    }

}
