package com.shakticoin.app.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegViewModel : ViewModel() {

    /** Page IDs for sign up activity. Each enum has corresponding fragment. */
    enum class Step {EnterEmail, VerifyEmail, EnterMobile, VerifyMobile, SetPassword}
    val currentStep: MutableLiveData<Step> = MutableLiveData(Step.EnterEmail);

    val progressOn: MutableLiveData<Boolean> = MutableLiveData(false)

    val emailAddress: MutableLiveData<String> = MutableLiveData()
    val emailAddressError: MutableLiveData<String> = MutableLiveData()

    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val phoneNumberError: MutableLiveData<String> = MutableLiveData()

    val smsSecurityCode: MutableLiveData<String> = MutableLiveData()

    val password1: MutableLiveData<String> = MutableLiveData()
    val password2: MutableLiveData<String> = MutableLiveData()
    val password1Error: MutableLiveData<String> = MutableLiveData()
    val password2Error: MutableLiveData<String> = MutableLiveData()
    val hasUppercase: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasLowercase: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasDigit: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasSymbol: MutableLiveData<Boolean> = MutableLiveData(false)
}