package com.shakticoin.app.api.wallet;

import androidx.annotation.Nullable;

import com.shakticoin.app.api.Constants;

public class WalletModelRequest {
    private String authorizationBytes;
    private String passphrase;

    public WalletModelRequest() {
        authorizationBytes = "";
        passphrase = "";
    }

    public WalletModelRequest(@Nullable String authorizationBytes, @Nullable String passphrase) {
        this.authorizationBytes = authorizationBytes != null ? authorizationBytes : "";
        this.passphrase = passphrase != null ? passphrase : Constants.PASSPHRASE;
    }

    public String getAuthorizationBytes() {
        return authorizationBytes;
    }

    public void setAuthorizationBytes(String authorizationBytes) {
        this.authorizationBytes = authorizationBytes;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
