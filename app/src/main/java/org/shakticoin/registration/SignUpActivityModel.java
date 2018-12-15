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
import org.shakticoin.widget.InlineLabelSpinner;

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

        CreateUserRequest request = new CreateUserRequest();
        request.setFirst_name(firstName.getValue());
        request.setLast_name(lastName.getValue());
        request.setEmail(emailAddress.getValue());
        request.setPhone_number(phoneNumber.getValue());
        request.setUser_type("MN");
        Country currentCountry = countryCode.get();
        if (currentCountry != null) {
            request.setCurrent_country(currentCountry.getCode());
        }
        Country citizenshipCountry = citizenshipCode.get();
        if (citizenshipCountry != null) {
            request.setCitizenship(Collections.singletonList(citizenshipCountry.getCode()));
        }
        request.setPostal_code(postalCode.getValue());
        request.setStreet_and_number(address.getValue());
        request.setCity(city.getValue());
        request.setState(state.getValue());
        request.setPassword1(newPassword.getValue());
        request.setPassword2(verifyPassword.getValue());

        MinerRepository repository = new MinerRepository();
        repository.createUser(request, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                progressBarVisibility.set(View.INVISIBLE);
                if (listener != null) listener.onComplete(value, error);
            }
        });
    }
}
