package org.shakticoin.registration;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableInt;
import android.view.View;

import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.auth.AuthRepository;
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

    private String currentCountryCode;
    private String currentCitizenshipCode;

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
                currentCountryCode = country.getCode();
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
                currentCitizenshipCode = country.getCode();
            }
        }
    }

    void createUser(OnCompleteListener listener) {
        progressBarVisibility.set(View.VISIBLE);
        //TODO: we must have fields for missing data in SignUpActivity
        CreateUserRequest request = new CreateUserRequest();
        request.setFirst_name(firstName.getValue());
        request.setLast_name(lastName.getValue());
        request.setEmail(emailAddress.getValue());
        request.setUser_type("MN"); //TODO: must be more types later
        request.setCurrent_country(currentCountryCode);
        request.setCitizenship(Collections.singletonList(currentCitizenshipCode));
        // TODO: postal code?
        request.setStreet_and_number(address.getValue());
        request.setCity(city.getValue());
        request.setPassword1(newPassword.getValue());
        request.setPassword2(verifyPassword.getValue());

        MinerRepository repository = new MinerRepository();
        repository.createUser(request, new OnCompleteListener() {
            @Override
            public void onComplete(Throwable error) {
                if (error != null) {
                    progressBarVisibility.set(View.INVISIBLE);
                    if (listener != null) listener.onComplete(error);
                    return;
                }

                AuthRepository authRepository = new AuthRepository();
                String username = emailAddress.getValue();
                String password = newPassword.getValue();
                if (username != null && password != null) {
                    authRepository.login(emailAddress.getValue(), newPassword.getValue(), new OnCompleteListener() {
                        @Override
                        public void onComplete(Throwable error) {
                            if (error != null) {
                                progressBarVisibility.set(View.INVISIBLE);
                                if (listener != null) listener.onComplete(error);
                                return;
                            }

                            MinerRepository minerRepository = new MinerRepository();
                            minerRepository.getUserInfo(new OnCompleteListener() {
                                @Override
                                public void onComplete(Throwable error) {
                                    progressBarVisibility.set(View.INVISIBLE);
                                    if (listener != null) listener.onComplete(error);
                                }
                            });
                        }
                    });
                } else {
                    progressBarVisibility.set(View.INVISIBLE);
                }
            }
        });
    }
}
