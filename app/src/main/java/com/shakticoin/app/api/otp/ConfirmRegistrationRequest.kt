package com.shakticoin.app.api.otp

data class ConfirmRegistrationRequest(val countryCode: String, val mobileNo: String, val otp: String)