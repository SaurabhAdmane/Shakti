package com.shakticoin.app.profile;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PersonalInfoViewModel extends ViewModel {
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> middleName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>(Collections.emptyList());;
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

    public MutableLiveData<List<String>> educationLevelList = new MutableLiveData<>();
    public MutableLiveData<String> selectedEducationLevel = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> occupation = new MutableLiveData<>();

    public MutableLiveData<String> kinFullName = new MutableLiveData<>();
    public MutableLiveData<String> kinContact = new MutableLiveData<>();
    public MutableLiveData<String> kinRelationship = new MutableLiveData<>();
    public MutableLiveData<String> kinSelectedEducationLevel = new MutableLiveData<>();

    // validation error messages
    public MutableLiveData<String> emailAddressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> occupationErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinFullNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinContactErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinRelationshipErrMsg = new MutableLiveData<>();

    //FIXME: uses obsolete userserve User class
    //    public MutableLiveData<User> user;

    public PersonalInfoViewModel() {
        CountryRepository repository = new CountryRepository();
        countryList = repository.getCountries();

        educationLevelList.setValue(Arrays.asList(
                ShaktiApplication.getContext().getResources().getStringArray(R.array.education_levels)));
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
            selectedState.setValue((Subdivision) spinner.getAdapter().getItem(position));
        }
    }

    /** This method is bind to onItemSelected event of Spinner */
    public void onEducationLevelSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }

    public void onEducationLevelSelectedKin(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            kinSelectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }
}
