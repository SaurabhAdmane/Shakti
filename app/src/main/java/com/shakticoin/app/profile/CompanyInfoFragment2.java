package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.databinding.FragmentCompanyInfoPage2Binding;

import java.util.List;

public class CompanyInfoFragment2 extends Fragment {
    private FragmentCompanyInfoPage2Binding binding;
    private CompanyProfileViewModel viewModel;
    private CountryRepository countryRepository = new CountryRepository();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CompanyProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompanyInfoPage2Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        viewModel.selectedCountry.observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                countryRepository.getSubdivisionsByCountry(country.getCode(), new OnCompleteListener<List<Subdivision>>() {
                    @Override
                    public void onComplete(List<Subdivision> value, Throwable error) {
                        if (error != null) {
                            return;
                        }
                        viewModel.selectedStateProvince.setValue(null);
                        viewModel.stateProvinceList.setValue(value);
                        binding.statesLayout.setVisibility(value != null && value.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });

        return v;
    }
}
