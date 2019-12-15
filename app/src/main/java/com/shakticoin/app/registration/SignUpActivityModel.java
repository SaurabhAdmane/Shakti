package com.shakticoin.app.registration;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.view.View;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.user.Citizenship;
import com.shakticoin.app.api.user.CreateUserParameters;
import com.shakticoin.app.api.user.Residence;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class SignUpActivityModel extends ViewModel {
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    MutableLiveData<String> firstNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    MutableLiveData<String> lastNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    MutableLiveData<String> emailAddressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    MutableLiveData<String> phoneNumberErrMsg = new MutableLiveData<>();

    public LiveData<List<Country>> countryList;
    public ObservableField<Country> countryCode = new ObservableField<>();
    MutableLiveData<String> countryCodeErrMsg = new MutableLiveData<>();
    public ObservableField<Country> citizenshipCode = new ObservableField<>();
    MutableLiveData<String> citizenshipCodeErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();
    MutableLiveData<String> postalCodeErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    MutableLiveData<String> cityErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();
    MutableLiveData<String> addressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> state = new MutableLiveData<>();
    MutableLiveData<String> stateErrMsg = new MutableLiveData<>();

    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    MutableLiveData<String> newPasswordErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> verifyPassword = new MutableLiveData<>();
    MutableLiveData<String> verifyPasswordErrMsg = new MutableLiveData<>();

    /** A trigger for progress indicator */
    public ObservableInt progressBarVisibility = new ObservableInt(View.INVISIBLE);

    void initCountryList(Locale locale) {
        if (countryList == null) {
            CountryRepository repository = new CountryRepository();
            countryList = repository.getCountries(locale);
        }
    }

    /** This method is bind to onItemSelected event */
    public void onCountrySelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            countryCode.set((Country) spinner.getAdapter().getItem(position));
        }
    }

    /** This method is bind to onItemSelected event */
    public void onCitizenshipSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            citizenshipCode.set((Country) spinner.getAdapter().getItem(position));
        }
    }

    void createUser(OnCompleteListener listener) {
        progressBarVisibility.set(View.VISIBLE);

        CreateUserParameters request = new CreateUserParameters();
        request.setFirst_name(firstName.getValue());
        request.setLast_name(lastName.getValue());
        request.setEmail(emailAddress.getValue());
        request.setMobile(phoneNumber.getValue());
        request.setPassword(newPassword.getValue());

        Country currentCountry = countryCode.get();
        Residence residence = new Residence();
        residence.setZip_code(postalCode.getValue());
        residence.setAddress_line_1(address.getValue());
        residence.setCity(city.getValue());
        if (currentCountry != null) {
            residence.setCountry_code(currentCountry.getCode());
            residence.setCountry_name(currentCountry.getName());
        }
//        request.setState(state.getValue());

        Citizenship citizenship = new Citizenship();
        Country citizenshipCountry = citizenshipCode.get();
        if (citizenshipCountry != null) {
            citizenship.setCountry_code(citizenshipCountry.getCode());
            citizenship.setCountry_name(citizenshipCountry.getName());
        }

        UserRepository repository = new UserRepository();
        repository.createUser(request, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                progressBarVisibility.set(View.INVISIBLE);
                if (listener != null) listener.onComplete(value, error);
            }
        });
    }
}
