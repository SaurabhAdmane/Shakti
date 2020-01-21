package com.shakticoin.app.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.user.User;

public class SettingsPersonalViewModel extends ViewModel {

    public MutableLiveData<User> user = new MutableLiveData<>();

}
