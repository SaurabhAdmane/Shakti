package com.shakticoin.app.settings;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.widget.InlineLabelSpinner;


public class ContactUsViewModel extends ViewModel {
//    public MutableLiveData<List<RequestReason>> reasonList = new MutableLiveData<>();
//    public MutableLiveData<RequestReason> selectedReason = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress =  new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    public ContactUsViewModel() {
    }

    public void onReasonSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
//            selectedReason.setValue((RequestReason) spinner.getAdapter().getItem(position));
        }
    }
}
