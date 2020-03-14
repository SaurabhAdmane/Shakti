package com.shakticoin.app.feats;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.List;

public class OnboardVerifierViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    public LiveData<List<Country>> countryList;

    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();

    private CountryRepository countryRepository = new CountryRepository();

    public OnboardVerifierViewModel() {
        countryList = countryRepository.getCountries();
    }

    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }
}
