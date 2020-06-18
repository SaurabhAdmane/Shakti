package com.shakticoin.app.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsPersonalViewModel extends ViewModel {

    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> middleName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();

}
