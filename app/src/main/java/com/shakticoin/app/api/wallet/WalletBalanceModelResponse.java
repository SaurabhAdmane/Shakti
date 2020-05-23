package com.shakticoin.app.api.wallet;

public class WalletBalanceModelResponse {
    private String walletBalance;
    private String message;

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
