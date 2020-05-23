package com.shakticoin.app.api.wallet;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.util.Debug;

public class WalletBalanceModelRequest {
    private Integer addressNumber = 0;
    private String cacheBytes = "";
    private Integer network = Debug.NETWORK;
    private Long sessionToken;

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

    public Integer getNetwork() {
        return network;
    }

    public void setNetwork(Integer network) {
        this.network = network;
    }

    public Long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Long sessionToken) {
        this.sessionToken = sessionToken;
    }
}
