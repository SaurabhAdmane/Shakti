package com.shakticoin.app.api.wallet;

import com.shakticoin.app.BuildConfig;

public class WalletAddressParameters {
    private Integer addressNumber;
    private String cacheBytes;
    private Integer network;
    private String passphrase;
    private String walletBytes;

    public WalletAddressParameters() {
        // 0 - main network, 1 - test network
        network = BuildConfig.DEBUG ? 1 : 0;
    }

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
