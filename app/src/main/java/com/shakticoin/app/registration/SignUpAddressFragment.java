package com.shakticoin.app.registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.databinding.FragmentSignupAddressBinding;
import com.shakticoin.app.util.PostalCodeValidator;

import java.util.Collections;
import java.util.List;

public class SignUpAddressFragment extends Fragment {
    private SignUpActivityModel viewModel;
    private FragmentSignupAddressBinding binding;
    private CountryRepository countryRepo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(SignUpActivityModel.class);
        }
        countryRepo = new CountryRepository();
        countryRepo.setLifecycleOwner(this);
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
                if (country != null) {
                    binding.postalCodeLayout.setValidator(new PostalCodeValidator(country.getCode()));
                    countryRepo.getSubdivisionsByCountry(country.getCode(), new OnCompleteListener<List<Subdivision>>() {
                        @Override
                        public void onComplete(List<Subdivision> value, Throwable error) {
                            if (error != null) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            viewModel.stateProvinceList.setValue(value);
                            binding.stateLayout.setVisibility(value != null && value.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    binding.postalCodeLayout.setValidator(new PostalCodeValidator(null));
                    viewModel.stateProvinceList.setValue(Collections.emptyList());
                    binding.stateLayout.setVisibility(View.GONE);
                }
            }
        });

        // display error callout for the field if error message is set
        viewModel.countryCodeErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.countriesLayout.setError(s);
                viewModel.countryCodeErrMsg.setValue(null);
            }
        });
        viewModel.citizenshipCodeErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.citizenshipLayout.setError(s);
                viewModel.citizenshipCodeErrMsg.setValue(null);
            }
        });
        viewModel.addressErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.addressLayout.setError(s);
                viewModel.addressErrMsg.setValue(null);
            }
        });
        viewModel.cityErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.cityLayout.setError(s);
                viewModel.cityErrMsg.setValue(null);
            }
        });
        viewModel.postalCodeErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.postalCodeLayout.setError(s);
                viewModel.postalCodeErrMsg.setValue(null);
            }
        });
        viewModel.stateErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.stateLayout.setError(s);
                viewModel.stateErrMsg.setValue(null);
            }
        });

        return binding.getRoot();
    }

}
