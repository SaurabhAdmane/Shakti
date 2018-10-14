package org.shakticoin.registration;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.shakticoin.api.country.Country;
import org.shakticoin.api.country.CountryRepository;

import java.util.List;
import java.util.Locale;


public class SignUpModel extends ViewModel {
    public LiveData<List<Country>> countryList;
    public MutableLiveData<Integer> currentCountry = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private String currentCountryCode;

    void initCountryList(Locale locale) {
        if (countryList == null) {
            CountryRepository repository = new CountryRepository();
            countryList = repository.getCountries(locale);
        }
    }

    public void onCountrySelected(int position) {
        // exclude first item that is a hint
        if (position > 0) {
            int i = position - 1;
            List<Country> list = countryList.getValue();
            if (list != null && list.size() > i) {
                Country country = list.get(i);
                currentCountryCode = country.getCode();
            }
        }
        currentCountry.setValue(position);
    }

    String getCurrentCountryCode() {
        return currentCountryCode;
    }
}
