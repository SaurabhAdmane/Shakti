package com.shakticoin.app.profile;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.ProgressBarModel;

public class PersonalViewModel extends ViewModel implements ProgressBarModel {
    private ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    @Override
    public ObservableBoolean getProgressBarTrigger() {
        return progressBarTrigger;
    }
}
