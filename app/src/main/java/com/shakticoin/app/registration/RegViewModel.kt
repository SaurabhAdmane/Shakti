package com.shakticoin.app.registration

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shakticoin.app.api.otp.IntlPhoneCountryCode
import com.shakticoin.app.widget.InlineLabelSpinner

class RegViewModel : ViewModel() {

    /** Page IDs for sign up activity. Each enum has corresponding fragment. */
    enum class Step {EnterEmail, VerifyEmail, EnterMobile, VerifyMobile, SetPassword}
    val currentStep: MutableLiveData<Step> = MutableLiveData(Step.EnterEmail);

    val progressOn: MutableLiveData<Boolean> = MutableLiveData(false)

    val emailAddress: MutableLiveData<String> = MutableLiveData()
    val emailAddressError: MutableLiveData<String> = MutableLiveData()

    var countryCodes : LiveData<List<IntlPhoneCountryCode>?>? = null
    var selectedCountryCode : MutableLiveData<IntlPhoneCountryCode> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val phoneNumberError: MutableLiveData<String> = MutableLiveData()

    val smsSecurityCode: MutableLiveData<String> = MutableLiveData()
    val smsSecurityCodeError: MutableLiveData<String> = MutableLiveData()

    val password1: MutableLiveData<String> = MutableLiveData()
    val password2: MutableLiveData<String> = MutableLiveData()
    val password1Error: MutableLiveData<String> = MutableLiveData()
    val password2Error: MutableLiveData<String> = MutableLiveData()
    val hasUppercase: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasLowercase: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasDigit: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasSymbol: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onCountryCodeSelected(view: View, position: Int) {
        val spinner = view as InlineLabelSpinner
        if (spinner.isChoiceMade) {
            selectedCountryCode.setValue(spinner.adapter.getItem(position) as IntlPhoneCountryCode)
        }
    }
}