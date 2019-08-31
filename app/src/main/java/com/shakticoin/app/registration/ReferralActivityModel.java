package com.shakticoin.app.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReferralActivityModel extends ViewModel {
    public MutableLiveData<String> fullName = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public MutableLiveData<String> referralCode = new MutableLiveData<>();

}
