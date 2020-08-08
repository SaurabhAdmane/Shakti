package com.shakticoin.app.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.bounty.OfferModel;

public class BonusBountyModel extends ViewModel {
    public MutableLiveData<OfferModel> selectedBonus = new MutableLiveData<>();

}
