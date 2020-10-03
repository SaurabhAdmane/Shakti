package com.shakticoin.app.profile;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.Country;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.Subdivision;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.List;

public class CompanyProfileViewModel extends ViewModel {

    public MutableLiveData<Boolean> progressBarTrigger = new MutableLiveData<>(false);

    public MutableLiveData<String> companyName = new MutableLiveData<>();
    public MutableLiveData<String> tradeName = new MutableLiveData<>();
    public MutableLiveData<String> establishmentDate = new MutableLiveData<>();
    public MutableLiveData<String> industryCode = new MutableLiveData<>();
    public MutableLiveData<Boolean> bindTradeName = new MutableLiveData<>(true);
    public MutableLiveData<String> vaultId = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();

    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>();
    public MutableLiveData<Subdivision> selectedStateProvince = new MutableLiveData<>();

    public CompanyProfileViewModel() {
        LicenseRepository countryRepository = new LicenseRepository();
        countryList = countryRepository.getCountries();
    }

    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }

    public void onStateProvinceSelected(View v, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) v;
        if (spinner.isChoiceMade()) {
            selectedStateProvince.setValue((Subdivision) spinner.getAdapter().getItem(position));
        }
    }
}
