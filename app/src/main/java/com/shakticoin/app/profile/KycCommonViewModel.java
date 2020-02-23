package com.shakticoin.app.profile;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.kyc.KycCategory;

import java.util.List;

public class KycCommonViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public MutableLiveData<KycCategory> kycCategory = new MutableLiveData<>();

    /** Read-only list of KYC categories */
    public List<KycCategory> kycCategories;
}
