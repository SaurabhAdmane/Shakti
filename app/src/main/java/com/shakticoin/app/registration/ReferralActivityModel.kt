package com.shakticoin.app.registration

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shakticoin.app.api.otp.IntlPhoneCountryCode
import com.shakticoin.app.api.otp.PhoneOTPRepository
import com.shakticoin.app.widget.InlineLabelSpinner

class ReferralActivityModel : ViewModel() {
    var emailAddress : MutableLiveData<String> = MutableLiveData()
    var mobileNumber : MutableLiveData<String> = MutableLiveData()
    var countryCodes : LiveData<List<IntlPhoneCountryCode>?>? = PhoneOTPRepository().getCountryCodeList()
    var selectedCountryCode : MutableLiveData<IntlPhoneCountryCode> = MutableLiveData()

    fun onCountryCodeSelected(view: View, position: Int) {
        val spinner = view as InlineLabelSpinner
        if (spinner.isChoiceMade) {
            selectedCountryCode.setValue(spinner.adapter.getItem(position) as IntlPhoneCountryCode)
        }
    }
}