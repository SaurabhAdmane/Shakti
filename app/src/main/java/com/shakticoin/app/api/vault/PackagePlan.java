package com.shakticoin.app.api.vault;

import java.math.BigDecimal;

public class PackagePlan {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Package getPackage() {
        return _package;
    }

    public void setPackage(Package _package) {
        this._package = _package;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFiat_price() {
        return fiat_price;
    }

    public void setFiat_price(BigDecimal fiat_price) {
        this.fiat_price = fiat_price;
    }

    public String getZoho_plan_code() {
        return zoho_plan_code;
    }

    public void setZoho_plan_code(String zoho_plan_code) {
        this.zoho_plan_code = zoho_plan_code;
    }

    public enum Period {Weekly, Monthly, Yearly}
    private Integer id;
    private Package _package;
    private Integer period;
    private String description;
    private BigDecimal fiat_price;
    private String zoho_plan_code;
}
