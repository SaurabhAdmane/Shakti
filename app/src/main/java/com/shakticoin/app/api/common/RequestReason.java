package com.shakticoin.app.api.common;

import androidx.annotation.NonNull;

public class RequestReason {
    private Integer id;
    private String reason;
    private String description;
    private Integer order_no;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }

    @NonNull
    @Override
    public String toString() {
        return reason != null ? reason : "";
    }
}
