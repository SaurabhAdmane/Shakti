package com.shakticoin.app.api.wallet;

public class CoinModel {
    private Integer addressNumber = 0;
    private String cacheBytes = "";
    private String messageForRecipient;
    private Long sessionToken;
    private String toAddress;
    private String valueInToshi;

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

    public Long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Long sessionToken) {
        this.sessionToken = sessionToken;
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
}