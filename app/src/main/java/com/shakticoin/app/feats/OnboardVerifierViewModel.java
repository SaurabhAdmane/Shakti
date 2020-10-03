package com.shakticoin.app.feats;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.Country;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.List;

public class OnboardVerifierViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    public LiveData<List<Country>> countryList;

    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();

    private LicenseRepository licenseRepository = new LicenseRepository();

    public OnboardVerifierViewModel() {
        countryList = licenseRepository.getCountries();
    }

    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }
}
