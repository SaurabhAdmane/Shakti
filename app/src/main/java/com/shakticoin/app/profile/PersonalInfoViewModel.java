package com.shakticoin.app.profile;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.Country;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.Subdivision;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.Collections;
import java.util.List;

public class PersonalInfoViewModel extends ViewModel {
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> middleName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>(Collections.emptyList());
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<Subdivision> selectedState = new MutableLiveData<>();
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

    public MutableLiveData<String> educationLevel = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> occupation = new MutableLiveData<>();

    public ObservableBoolean subscriptionConfirmed = new ObservableBoolean(false);

    public MutableLiveData<String> kinContact = new MutableLiveData<>();

    // validation error messages
    public MutableLiveData<String> emailAddressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> occupationErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinContactErrMsg = new MutableLiveData<>();

    // enable/disable triggers
    public ObservableBoolean nextToSecondPersonalPage = new ObservableBoolean(false);

    public PersonalInfoViewModel() {
        LicenseRepository repository = new LicenseRepository();
        countryList = repository.getCountries();
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
        Object selection = spinner.getAdapter().getItem(position);
        if (spinner.isChoiceMade() && selection instanceof Subdivision) {
            selectedState.setValue((Subdivision) selection);
        }
    }

    public void onSubscribeConfirmed(boolean isChecked) {
        if (subscriptionConfirmed.get() != isChecked) {
            subscriptionConfirmed.set(isChecked);
        }
    }
}
