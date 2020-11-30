package com.shakticoin.app.api.wallet;

import com.shakticoin.app.wallet.BlockByTime;

public class SessionModelResponse {
    private Long sessionToken;
    private String message;

    public BlockByTime blockByTime;

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




