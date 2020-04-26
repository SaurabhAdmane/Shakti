package com.shakticoin.app.api.wallet;

public class TransferParameters {
    private Integer addressNumber;
    private String cacheBytes;
    private String messageForRecipient;
    private String passphrase;
    private String toAddress;
    private String valueInToshi;
    private String walletBytes;

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getCacheBytes() {
        return cacheBytes;
    }

    public void setCacheBytes(String cacheBytes) {
        this.cacheBytes = cacheBytes;
    }

    public String getMessageForRecipient() {
        return messageForRecipient;
    }

    public void setMessageForRecipient(String messageForRecipient) {
        this.messageForRecipient = messageForRecipient;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getValueInToshi() {
        return valueInToshi;
    }

    public void setValueInToshi(String valueInToshi) {
        this.valueInToshi = valueInToshi;
    }

    public String getWalletBytes() {
        return walletBytes;
    }

    public void setWalletBytes(String walletBytes) {
        this.walletBytes = walletBytes;
    }
}
