package com.shakticoin.app.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewBonusBountyModel extends ViewModel {

    public MutableLiveData<String> balance = new MutableLiveData<>();

    public NewBonusBountyModel() {
        // TODO: get data from server
        balance.setValue("SXE 1111.00");
    }

}
