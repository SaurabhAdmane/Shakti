package com.shakticoin.app.api.otp

class PhoneOTPVerifyRequest : OTPVerifyRequest() {
    var mobileNo: String? = null;
    var countryCode: String? = null;
}