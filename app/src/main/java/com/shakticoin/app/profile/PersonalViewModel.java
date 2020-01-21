package com.shakticoin.app.profile;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.user.User;

public class PersonalViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
}
