package com.shakticoin.app.feats;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.List;

public class OnboardProviderViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public ObservableBoolean stateProvinceVisibilityTrigger = new ObservableBoolean(false);

    public LiveData<List<Country>> countryList;

    public MutableLiveData<String> serviceProviderWalletId = new MutableLiveData<>();
    public MutableLiveData<String> serviceProviderBizVaultId = new MutableLiveData<>();
    public MutableLiveData<Country> selectedTaxCountry = new MutableLiveData<>();
    public MutableLiveData<String> primaryEmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> primaryPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> businessName = new MutableLiveData<>();

    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>();
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<Subdivision> selectedStateProvince = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> address1 = new MutableLiveData<>();
    public MutableLiveData<String> address2 = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();

    private CountryRepository countryRepository = new CountryRepository();

    public OnboardProviderViewModel() {
        // TODO: demo data - to remove
        // End of demo data

        countryList = countryRepository.getCountries();
    }

    public void onTaxCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedTaxCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }

    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            Country country = (Country) spinner.getAdapter().getItem(position);
            selectedCountry.setValue(country);
            countryRepository.getSubdivisionsByCountry(country.getCode(), new OnCompleteListener<List<Subdivision>>() {
                @Override
                public void onComplete(List<Subdivision> value, Throwable error) {
                    if (error != null) {
                        return;
                    }
                    stateProvinceList.setValue(value);
                    stateProvinceVisibilityTrigger.set(value != null && value.size() > 0);
                }
            });
        }
    }

    public void onStateProvinceSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            Object value = spinner.getAdapter().getItem(position);
            selectedStateProvince.setValue(value instanceof Subdivision ? (Subdivision)value : null);
        }
    }
}
