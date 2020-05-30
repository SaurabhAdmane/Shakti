package com.shakticoin.app.api.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WalletModelRequest {
    private String authorizationBytes;
    private String passphrase;

    public WalletModelRequest() {
        authorizationBytes = "";
        passphrase = "";
    }

    public WalletModelRequest(@Nullable String authorizationBytes, @NonNull String passphrase) {
        this.authorizationBytes = authorizationBytes != null ? authorizationBytes : "";
        this.passphrase = passphrase;
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
