package com.shakticoin.app.api.wallet;

public class WalletAddressModelResponse {
    private String walletAddress;
    private String message;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
