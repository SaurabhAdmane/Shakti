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

public class OnboardSchoolViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    public MutableLiveData<String> schoolName = new MutableLiveData<>();
    public LiveData<List<Country>> countryList;
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<String> establishedDate = new MutableLiveData<>();
    public MutableLiveData<String> bizVaultId = new MutableLiveData<>();
    public MutableLiveData<String> walletId = new MutableLiveData<>();
    public MutableLiveData<String> primaryLanguage = new MutableLiveData<>();
    public MutableLiveData<String> secondaryLanguage = new MutableLiveData<>();

    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> registrar = new MutableLiveData<>();
    public MutableLiveData<String> principal = new MutableLiveData<>();
    public MutableLiveData<String> vicePrincipal = new MutableLiveData<>();

    public OnboardSchoolViewModel() {
        LicenseRepository licenseRepository = new LicenseRepository();
        countryList = licenseRepository.getCountries();
    }

    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }
}
