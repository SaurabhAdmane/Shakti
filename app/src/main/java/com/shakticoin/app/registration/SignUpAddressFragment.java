package com.shakticoin.app.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.databinding.FragmentSignupAddressBinding;
import com.shakticoin.app.util.PostalCodeValidator;
import com.shakticoin.app.util.Validator;

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

        binding.postalCodeLayout.setValidator(new PostalCodeValidator(null));
        viewModel.countryCode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Country country = ((ObservableField<Country>) sender).get();
                binding.postalCodeLayout.setValidator(new PostalCodeValidator(country != null ? country.getCode() : null));
            }
        });

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
