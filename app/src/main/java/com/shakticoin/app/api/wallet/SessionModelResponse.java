package com.shakticoin.app.api.wallet;

public class SessionModelResponse {
    private Long sessionToken;
    private String message;

    public Long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Long sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
