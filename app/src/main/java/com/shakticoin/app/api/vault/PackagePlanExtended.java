package com.shakticoin.app.api.vault;

import java.math.BigDecimal;
import java.util.List;

public class PackagePlanExtended {
    private String id;
    private String period;
    private String description;
    private BigDecimal fiat_price;
    private String zoho_plan_code;
    private List<PackageDiscount> discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
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

    public List<PackageDiscount> getDiscount() {
        return discount;
    }

    public void setDiscount(List<PackageDiscount> discount) {
        this.discount = discount;
    }
}
