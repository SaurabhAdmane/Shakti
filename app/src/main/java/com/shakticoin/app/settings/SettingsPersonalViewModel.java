package com.shakticoin.app.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsPersonalViewModel extends ViewModel {

    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> middleName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();
    public MutableLiveData<String> occupation = new MutableLiveData<>();
    public MutableLiveData<String> education = new MutableLiveData<>();

    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    public MutableLiveData<String> address1 = new MutableLiveData<>();
    public MutableLiveData<String> address2 = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> stateProvince = new MutableLiveData<>();
    public MutableLiveData<String> postalCode = new MutableLiveData<>();
    public MutableLiveData<String> country = new MutableLiveData<>();

}
