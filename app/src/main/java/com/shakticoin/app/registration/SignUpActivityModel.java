package com.shakticoin.app.registration;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.api.user.Citizenship;
import com.shakticoin.app.api.user.CreateUserParameters;
import com.shakticoin.app.api.user.Residence;
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.util.Debug;
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
    public MutableLiveData<List<Subdivision>> stateProvinceList = new MutableLiveData<>(Collections.emptyList());
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
    MutableLiveData<String> stateErrMsg = new MutableLiveData<>();
    public ObservableField<Subdivision> stateProvinceCode = new ObservableField<>();

    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    MutableLiveData<String> newPasswordErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> verifyPassword = new MutableLiveData<>();
    MutableLiveData<String> verifyPasswordErrMsg = new MutableLiveData<>();

    /** A trigger for progress indicator */
    public ObservableInt progressBarVisibility = new ObservableInt(View.INVISIBLE);

    public ObservableBoolean isPhoneNumberChecked = new ObservableBoolean(false);

    void initCountryList(Locale locale) {
        if (countryList == null) {
            CountryRepository repository = new CountryRepository();
            countryList = repository.getCountries();
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

    /** This method is bind to onItemSelected event */
    public void onStateProvinceSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            stateProvinceCode.set((Subdivision) spinner.getAdapter().getItem(position));
        }
    }

    void createUser(OnCompleteListener listener) {
        progressBarVisibility.set(View.VISIBLE);

        CreateUserParameters request = new CreateUserParameters();
        request.setFirst_name(firstName.getValue());
        request.setLast_name(lastName.getValue());
        request.setMobile(phoneNumber.getValue());
        request.setEmail(emailAddress.getValue());
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
        Subdivision stateProvince = stateProvinceCode.get();
        if (stateProvince != null) {
            residence.setSubdivision_id(stateProvince.getId());
            residence.setSubdivision_name(stateProvince.getName());
        }
        request.setResidence(Collections.singletonList(residence));

        Citizenship citizenship = new Citizenship();
        Country citizenshipCountry = citizenshipCode.get();
        if (citizenshipCountry != null) {
            citizenship.setCountry_code(citizenshipCountry.getCode());
            citizenship.setCountry_name(citizenshipCountry.getName());
        }
        request.setCitizenship(Collections.singletonList(citizenship));

        UserRepository repository = new UserRepository();
        repository.createUser(request, new OnCompleteListener<User>() {
            @Override
            public void onComplete(User value, Throwable error) {
                progressBarVisibility.set(View.INVISIBLE);
                if (error != null) {
                    Debug.logException(error);
                }
                if (listener != null) listener.onComplete(value, error);
            }
        });
    }
}
