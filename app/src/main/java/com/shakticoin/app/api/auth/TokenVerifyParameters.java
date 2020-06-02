package com.shakticoin.app.api.auth;

public class TokenVerifyParameters {
    private String token;

    public TokenVerifyParameters() {}

    public TokenVerifyParameters(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
