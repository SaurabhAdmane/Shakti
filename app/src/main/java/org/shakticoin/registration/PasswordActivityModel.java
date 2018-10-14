package org.shakticoin.registration;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.miner.CreateUserRequest;
import org.shakticoin.api.miner.MinerRepository;

import java.util.Collections;


public class PasswordActivityModel extends ViewModel {
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> verifyPassword = new MutableLiveData<>();

    private String emailAddress;
    private String phoneNumber;
    private String currentCountry;
    private String postalCode;

    public void onRegister(OnCompleteListener listener) {
        //TODO: we must have fields for missing data in SignUpActivity
        CreateUserRequest request = new CreateUserRequest();
        request.setFirst_name("Peter");
        request.setLast_name("Parker");
        request.setEmail(emailAddress);
        request.setUser_type("MN");
        request.setPassword1(newPassword.getValue());
        request.setPassword2(verifyPassword.getValue());
        request.setCurrent_country(currentCountry);
        request.setCity("Gotham");
        request.setStreet_and_number("First St, 5");
        request.setCitizenship(Collections.singletonList(currentCountry)); // TODO: temporarily, must be separate input

        MinerRepository repository = new MinerRepository();
        repository.createUser(request, listener);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
