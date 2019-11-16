package com.shakticoin.app.profile;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.widget.InlineLabelSpinner;

import java.util.ArrayList;
import java.util.List;

public class AdditionalInfoViewModel extends ViewModel {
    public MutableLiveData<List<String>> educatinLevelList = new MutableLiveData<>();
    public MutableLiveData<String> selectedEducationLevel = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> occupation = new MutableLiveData<>();

    public MutableLiveData<String> kinFullName = new MutableLiveData<>();
    public MutableLiveData<String> kinContact = new MutableLiveData<>();
    public MutableLiveData<String> kinRelationship = new MutableLiveData<>();
    public MutableLiveData<String> kinSelectedEducationLevel = new MutableLiveData<>();

    // validation error messages
    public MutableLiveData<String> emailAddressErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> occupationErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinFullNameErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinContactErrMsg = new MutableLiveData<>();
    public MutableLiveData<String> kinRelationshipErrMsg = new MutableLiveData<>();

    public AdditionalInfoViewModel() {
        // TODO: replace test set of education levels with final
        List<String> list = new ArrayList<>();
        list.add("Associate degree");
        list.add("Bachelor's degree");
        list.add("Master's degree");
        list.add("Doctoral degree");
        educatinLevelList.setValue(list);
    }

    /** This method is bind to onItemSelected event of Spinner */
    public void onEducationLevelSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }

    public void onEducationLevelSelectedKin(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            kinSelectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }
}
