package com.shakticoin.app.api.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SessionModelRequest {
    private String cacheBytes = "";
    private String passphrase;
    private String walletBytes;

    public SessionModelRequest() {}

    public SessionModelRequest(@Nullable String cacheBytes, @NonNull String passphrase, @NonNull String walletBytes) {
        this.cacheBytes = cacheBytes == null ? "" : cacheBytes;
        this.passphrase = passphrase;
        this.walletBytes = walletBytes;
    }

    public String getCacheBytes() {
        return cacheBytes;
    }

    public void setCacheBytes(String cacheBytes) {
        this.cacheBytes = cacheBytes;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getWalletBytes() {
        return walletBytes;
    }

    public void setWalletBytes(String walletBytes) {
        this.walletBytes = walletBytes;
    }
}
