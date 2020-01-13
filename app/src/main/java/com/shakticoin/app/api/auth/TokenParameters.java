package com.shakticoin.app.api.auth;

public class TokenParameters {
    private String access;
    private String refresh;

    public TokenParameters() {}

    public TokenParameters(String accessToken, String refreshToken) {
        access = accessToken;
        refresh = refreshToken;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
