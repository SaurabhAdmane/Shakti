package com.shakticoin.app.payment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.LicenseType;

public class PaymentOptionsViewModel extends ViewModel {
    public enum Plan {WEEKLY, MONTHLY, ANNUAL}
    public enum PackageType {M101, T100, T200, T300, T400}

    public MutableLiveData<LicenseType> selectedPackage = new MutableLiveData<>();

    public PaymentOptionsViewModel() {
    }
}
