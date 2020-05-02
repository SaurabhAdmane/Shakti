package com.shakticoin.app.api.wallet;

import com.shakticoin.app.api.Constants;

public class SessionModelRequest {
    private String cacheBytes = "";
    private String passphrase = Constants.PASSPHRASE;
    private String walletBytes;

    public SessionModelRequest() {}

    public SessionModelRequest(String cacheBytes, String passphrase, String walletBytes) {
        this.cacheBytes = cacheBytes;
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
