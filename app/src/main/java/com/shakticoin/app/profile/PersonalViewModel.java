package com.shakticoin.app.profile;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.ProgressBarModel;

public class PersonalViewModel extends ViewModel implements ProgressBarModel {
    private ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public MutableLiveData<String> shaktiId = new MutableLiveData<>();

    @Override
    public ObservableBoolean getProgressBarTrigger() {
        return progressBarTrigger;
    }
}
