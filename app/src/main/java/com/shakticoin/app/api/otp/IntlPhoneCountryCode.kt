package com.shakticoin.app.api.otp

data class IntlPhoneCountryCode(val countryCode: String, val country: String, val isoCode: String) {
    override fun toString(): String {
        return "$country ($countryCode)"
    }
}