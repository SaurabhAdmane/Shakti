package com.shakticoin.app.api.wallet;

public class WalletBytesModelResponse {
    private String walletBytes;
    private String message;

    public String getWalletBytes() {
        return walletBytes;
    }

    public void setWalletBytes(String walletBytes) {
        this.walletBytes = walletBytes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
