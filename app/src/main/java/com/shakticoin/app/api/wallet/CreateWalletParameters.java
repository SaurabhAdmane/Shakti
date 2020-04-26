package com.shakticoin.app.api.wallet;

import androidx.annotation.Nullable;

public class CreateWalletParameters {
    private String authorizationBytes;
    private String passphrase;

    public CreateWalletParameters() {
        authorizationBytes = "";
        passphrase = "";
    }

    public CreateWalletParameters(@Nullable String authorizationBytes, @Nullable String passphrase) {
        this.authorizationBytes = authorizationBytes != null ? authorizationBytes : "";
        this.passphrase = passphrase != null ? passphrase : "";
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
