package org.shakticoin.registration;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


public class RecoveryPasswordModel extends ViewModel {
    public final MutableLiveData<String> emailAddress = new MutableLiveData<>();
    private boolean requestSent = false;

    String getEmail() {
        return emailAddress.getValue();
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }
}
