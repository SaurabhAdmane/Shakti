package com.shakticoin.app.feats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.databinding.FragmentFeatsChildPage2Binding;
import com.shakticoin.app.util.PostalCodeValidator;

import java.util.Collections;
import java.util.List;

public class OnboardChildPage2Fragment extends Fragment {
    private FragmentFeatsChildPage2Binding binding;
    private OnboardChildViewModel viewModel;
    private CountryRepository countryRepo = new CountryRepository();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(OnboardChildViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeatsChildPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        viewModel.selectedCountry.observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
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
                            binding.stateProvinceLayout.setVisibility(value != null && value.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    binding.postalCodeLayout.setValidator(new PostalCodeValidator(null));
                    viewModel.stateProvinceList.setValue(Collections.emptyList());
                    binding.stateProvinceLayout.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }
}
