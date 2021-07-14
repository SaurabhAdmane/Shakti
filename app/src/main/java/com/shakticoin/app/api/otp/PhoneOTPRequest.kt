package com.shakticoin.app.api.otp

class PhoneOTPRequest : OTPRequest() {
    var mobileNo: String? = null
    var countryCode: String? = null
}