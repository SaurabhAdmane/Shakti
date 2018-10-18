package org.shakticoin.registration;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.country.Country;
import org.shakticoin.api.country.CountryRepository;
import org.shakticoin.api.miner.CreateUserRequest;
import org.shakticoin.api.miner.MinerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class SignUpActivityModel extends ViewModel {
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public MutableLiveData<String> postalCode = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();

    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> verifyPassword = new MutableLiveData<>();

    /** A trigger for progress indicator */
    public ObservableInt progressBarVisibility = new ObservableInt(View.INVISIBLE);

    public ObservableField<Country> countryCode = new ObservableField<>();
    public ObservableField<Country> citizenshipCode = new ObservableField<>();

    void initCountryList(Locale locale) {
        if (countryList == null) {
            CountryRepository repository = new CountryRepository();
            countryList = repository.getCountries(locale);
        }
    }

    /** This method is bind to onItemSelected event */
    public void onCountrySelected(int position) {
        // exclude first item that is a hint
        if (position > 0) {
            int i = position - 1;
            List<Country> list = countryList.getValue();
            if (list != null && list.size() > i) {
                Country country = list.get(i);
                countryCode.set(country);
            }
        }
    }

    /** This method is bind to onItemSelected event */
    public void onCitizenshipSelected(int position) {
        // exclude first item that is a hint
        if (position > 0) {
            int i = position - 1;
            List<Country> list = countryList.getValue();
            if (list != null && list.size() > i) {
                Country country = list.get(i);
                citizenshipCode.set(country);
            }
        }
    }

    void createUser(OnCompleteListener listener) {
        progressBarVisibility.set(View.VISIBLE);

        CreateUserRequest request = new CreateUserRequest();
        request.setFirst_name(firstName.getValue());
        request.setLast_name(lastName.getValue());
        request.setEmail(emailAddress.getValue());
        request.setUser_type("MN"); //TODO: must be more types later
        Country currentCountry = countryCode.get();
        if (currentCountry != null) {
            request.setCurrent_country(currentCountry.getCode());
        }
        Country citizenshipCountry = citizenshipCode.get();
        if (citizenshipCountry != null) {
            request.setCitizenship(Collections.singletonList(citizenshipCountry.getCode()));
        }
        // TODO: postal code?
        request.setStreet_and_number(address.getValue());
        request.setCity(city.getValue());
        request.setPassword1(newPassword.getValue());
        request.setPassword2(verifyPassword.getValue());

        MinerRepository repository = new MinerRepository();
        repository.createUser(request, new OnCompleteListener() {
            @Override
            public void onComplete(Throwable error) {
                progressBarVisibility.set(View.INVISIBLE);
                if (listener != null) listener.onComplete(error);
            }
        });
    }
}
