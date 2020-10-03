package com.shakticoin.app.feats;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.Country;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.Subdivision;
import com.shakticoin.app.api.misc.Language;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class OnboardChildViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<List<String>> gender = new MutableLiveData<>();
    public MutableLiveData<String> selectedGender = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();
    public MutableLiveData<List<Language>> languages = new MutableLiveData<>();
    public MutableLiveData<Language> selectedLanguage = new MutableLiveData<>();
    public MutableLiveData<String> primaryEmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> primaryPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> address1 = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>(Collections.emptyList());
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<Subdivision> selectedState = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> livingAddress1 = new MutableLiveData<>();
    public MutableLiveData<String> livingAddress2 = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();

    public MutableLiveData<String> secondaryEmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> secondaryPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> occupation = new MutableLiveData<>();
    public MutableLiveData<List<String>> educationLevelList = new MutableLiveData<>();
    public MutableLiveData<String> selectedEducationLevel = new MutableLiveData<>();

    public MutableLiveData<String> kinFirstName = new MutableLiveData<>();
    public MutableLiveData<String> kinLastName = new MutableLiveData<>();
    public MutableLiveData<List<String>> relationshipList = new MutableLiveData<>();
    public MutableLiveData<String> selectedRelationship = new MutableLiveData<>();

    public OnboardChildViewModel() {
        // TODO demo data - remove
        List<String> genderList = new ArrayList<>(2);
        genderList.add("Boy");
        genderList.add("Girl");
        gender.setValue(genderList);

        List<String> list = new ArrayList<>();
        list.add("Associate degree");
        list.add("Bachelor's degree");
        list.add("Master's degree");
        list.add("Doctoral degree");
        educationLevelList.setValue(list);

        List<String> relationships = new ArrayList<>();
        relationships.add("Option 1");
        relationships.add("Option 2");
        relationships.add("Option 3");
        relationshipList.setValue(relationships);
        // End of demo data

        // List of available languages
        List<Language> languages = new ArrayList<>();
        String[] languageCodes = Locale.getISOLanguages();
        for (String code : languageCodes) {
            languages.add(new Language(code));
        }
        this.languages.setValue(languages);

        // default selection should be the same as the current user's locale
        String currentLanguage = Locale.getDefault().getLanguage();
        for (Language lang : languages) {
            if (currentLanguage.equalsIgnoreCase(lang.getCode())) {
                selectedLanguage.setValue(lang);
            }
        }

        LicenseRepository repository = new LicenseRepository();
        countryList = repository.getCountries();
    }

    public void onGenderSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedGender.setValue((String) spinner.getAdapter().getItem(position));
        }
    }

    public void onOnLanguageSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedLanguage.setValue((Language) spinner.getAdapter().getItem(position));
        }
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

    public void onEducationSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }

    public void onRelationshipSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedRelationship.setValue((String) spinner.getAdapter().getItem(position));
        }
    }
}
