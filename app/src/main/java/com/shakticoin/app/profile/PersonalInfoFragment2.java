package com.shakticoin.app.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.Subdivision;
import com.shakticoin.app.databinding.FragmentProfilePersonalPage2Binding;
import com.shakticoin.app.util.PostalCodeValidator;

import java.util.Collections;
import java.util.List;

public class PersonalInfoFragment2 extends Fragment {
    public static final String TAG = PersonalInfoFragment2.class.getSimpleName();

    private FragmentProfilePersonalPage2Binding binding;
    private PersonalInfoViewModel viewModel;

    private LicenseRepository countryRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilePersonalPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(requireActivity()).get(PersonalInfoViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        countryRepo = new LicenseRepository();
        countryRepo.setLifecycleOwner(getViewLifecycleOwner());

        binding.postalCodeLayout.setValidator(new PostalCodeValidator(null));
        if (viewModel.selectedState.getValue() != null) {
            binding.stateProvinceLayout.setVisibility(View.VISIBLE);
        }

        // update list of states when selected country is changed.
        viewModel.selectedCountry.observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                binding.postalCodeLayout.setValidator(new PostalCodeValidator(country.getCountryCode()));
                countryRepo.getSubdivisionsByCountry(country.getCountryCode(), new OnCompleteListener<List<Subdivision>>() {
                    @Override
                    public void onComplete(List<Subdivision> value, Throwable error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        viewModel.stateProvinceList.setValue(value);
                        binding.stateProvinceLayout.setVisibility(value != null && value.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                binding.postalCodeLayout.setValidator(new PostalCodeValidator(null));
                viewModel.stateProvinceList.setValue(Collections.emptyList());
                binding.stateProvinceLayout.setVisibility(View.GONE);
            }

        });

        viewModel.countriesErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.countriesLayout.setError(s);
                viewModel.countriesErrMsg.setValue(null);
            }
        });
        viewModel.cityErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.cityLayout.setError(s);
                viewModel.cityErrMsg.setValue(null);
            }
        });
        viewModel.addressErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.address1Layout.setError(s);
                viewModel.addressErrMsg.setValue(null);
            }
        });
        viewModel.postalCodeErrMsg.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.postalCodeLayout.setError(s);
                viewModel.postalCodeErrMsg.setValue(null);
            }
        });

        return v;
    }
}
