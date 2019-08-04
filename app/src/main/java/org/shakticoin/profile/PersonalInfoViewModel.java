package org.shakticoin.profile;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.shakticoin.api.country.Country;
import org.shakticoin.api.country.CountryRepository;
import org.shakticoin.widget.InlineLabelSpinner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PersonalInfoViewModel extends ViewModel {
    public LiveData<List<Country>> countryList;
    public MutableLiveData<List<String>> stateProvinceList;
    public MutableLiveData<Country> selectedCountry = new MutableLiveData<>();
    public MutableLiveData<String> selectedState = new MutableLiveData<>();


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
