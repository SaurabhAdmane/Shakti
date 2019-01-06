package org.shakticoin.registration;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Observable;

public class NewBonusBountyModel extends ViewModel {

    public MutableLiveData<String> balance = new MutableLiveData<>();

    public NewBonusBountyModel() {
        // TODO: get data from server
        balance.setValue("SXE 1111.00");
    }

}
