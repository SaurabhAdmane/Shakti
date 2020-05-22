package com.shakticoin.app.api.wallet;

import com.shakticoin.app.BuildConfig;

public class WalletAddressModelRequest {
    private Integer addressNumber = 0;
    private String cacheBytes = "";
    private Integer network = 1;
    private Long sessionToken;

    public WalletAddressModelRequest() {
        // 0 - main network, 1 - test network
        network = BuildConfig.DEBUG ? 1 : 0;
    }

    /**
     * The addressNumber parameter is an unsigned integer representing the address number to return.
     * Address zero is always the user’s main address for the requested network.
     */
    public Integer getAddressNumber() {
        return addressNumber;
    }

    /**
     * The addressNumber parameter is an unsigned integer representing the address number to return.
     * Address zero is always the user’s main address for the requested network.
     */
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
