package com.shakticoin.app.api.otp

class EmailRegistrationRequest(confirmPath: String? = "", email: String) {
    var confirmPath: String? = confirmPath
    var email: String? = email
}