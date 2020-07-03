package com.shakticoin.app.profile;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.ProgressBarModel;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.api.kyc.KycDocType;

import java.util.List;

public class KycCommonViewModel extends ViewModel implements ProgressBarModel {
    private ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

    public MutableLiveData<KycCategory> kycCategory = new MutableLiveData<>();
    public KycDocType kycDocumentType;

    /** Read-only list of KYC categories */
    public List<KycCategory> kycCategories;

    /** Setting this variable to true triggers refreshing the list of files */
    public ObservableBoolean updateList = new ObservableBoolean(false);

    @Override
    public ObservableBoolean getProgressBarTrigger() {
        return progressBarTrigger;
    }
}
