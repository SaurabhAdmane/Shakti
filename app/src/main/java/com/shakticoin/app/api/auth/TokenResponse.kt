package com.shakticoin.app.api.auth

class TokenResponse {
    var refresh_token: String? = null
    var access_token: String? = null
    var scope: String? = null
    var token_type: String? = null
    var expires_in: Int? = null
}