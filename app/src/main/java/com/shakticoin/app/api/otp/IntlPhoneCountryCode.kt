package com.shakticoin.app.api.otp

data class IntlPhoneCountryCode(val countryCode: String, val countryName: String) {
    override fun toString(): String {
        return "$countryName ($countryCode)"
    }
}