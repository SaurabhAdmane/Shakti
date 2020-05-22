package com.shakticoin.app.api.wallet;

import com.shakticoin.app.util.Debug;

public class BlockByTimeRequest {
    private Integer includeTransactionDetails;
    private Integer network = Debug.NETWORK;
    private Long sessionToken;
    private Integer timestamp;

    public Integer getIncludeTransactionDetails() {
        return includeTransactionDetails;
    }

    public void setIncludeTransactionDetails(Integer includeTransactionDetails) {
        this.includeTransactionDetails = includeTransactionDetails;
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

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}


