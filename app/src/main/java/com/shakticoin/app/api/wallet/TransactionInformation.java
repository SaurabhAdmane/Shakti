package com.shakticoin.app.api.wallet;

public class TransactionInformation {
    private String identifier;
    private Long sessionToken;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Long sessionToken) {
        this.sessionToken = sessionToken;
    }
}
