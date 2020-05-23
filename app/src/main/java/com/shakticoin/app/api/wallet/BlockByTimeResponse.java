package com.shakticoin.app.api.wallet;

public class BlockByTimeResponse {
    private String blockByTime;
    private String message;

    public String getBlockByTime() {
        return blockByTime;
    }

    public void setBlockByTime(String blockByTime) {
        this.blockByTime = blockByTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
