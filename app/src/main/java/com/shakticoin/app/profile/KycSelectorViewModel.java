package com.shakticoin.app.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.kyc.KycCategory;

public class KycSelectorViewModel extends ViewModel {

    /** Selected KYC category */
    MutableLiveData<KycCategory> selectedCategory = new MutableLiveData<>();

}
