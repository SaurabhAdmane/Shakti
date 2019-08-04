package org.shakticoin.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class RecoveryPasswordModel extends ViewModel {
    public final MutableLiveData<String> emailAddress = new MutableLiveData<>();

    /**
     * The variable help to pass error message from activity to the fragment. Once fragment
     * catch the change it display error callout and reset value to null.
     */
    public MutableLiveData<String> emailAddressErrMsg = new MutableLiveData<>();

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
