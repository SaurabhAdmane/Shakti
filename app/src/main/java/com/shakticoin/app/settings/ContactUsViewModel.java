package com.shakticoin.app.settings;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.ArrayList;
import java.util.List;

public class ContactUsViewModel extends ViewModel {
    public MutableLiveData<List<String>> reasonList = new MutableLiveData<>();
    public MutableLiveData<String> selectedReason = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress =  new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    public ContactUsViewModel() {
        List<String> list = new ArrayList<>();
        list.add("Reason 1");
        list.add("Reason 2");
        list.add("Reason 3");
        reasonList.setValue(list);
    }

    public void onReasonSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedReason.setValue((String) spinner.getAdapter().getItem(position));
        }
    }
}
