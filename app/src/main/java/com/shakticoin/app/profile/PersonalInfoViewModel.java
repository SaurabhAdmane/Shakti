package com.shakticoin.app.profile;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PersonalInfoViewModel extends ViewModel {
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> middleName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<List<String>> stateProvinceList;
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<String> selectedState = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> address1 = new MutableLiveData<>();
    public MutableLiveData<String> address2 = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();

    // variables that pass error message from activity to the fragment
    public MutableLiveData<String> firstNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> lastNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> countriesErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> cityErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> addressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> postalCodeErrMsg = new MutableLiveData<>();

    public PersonalInfoViewModel() {
        if (countryList == null) {
            CountryRepository repository = new CountryRepository();
            countryList = repository.getCountries(Locale.getDefault());
        }

        stateProvinceList = new MutableLiveData<>();
        stateProvinceList.setValue(Arrays.asList("NY", "CA", "TX"));
    }

    /** This method is bind to onItemSelected event */
    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedCountry.setValue((Country) spinner.getAdapter().getItem(position));
        }
    }

    /** This method is bind to onItemSelected event */
    public void onStateSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedState.setValue((String) spinner.getAdapter().getItem(position));
        }
    }

}
