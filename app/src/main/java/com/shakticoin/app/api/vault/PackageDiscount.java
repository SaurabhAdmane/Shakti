package com.shakticoin.app.api.vault;

import java.math.BigDecimal;

public class PackageDiscount {
    private Integer id;
    private String description;
    private BigDecimal fiat_discount_price;
    private BigDecimal saving_percentage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFiat_discount_price() {
        return fiat_discount_price;
    }

    public void setFiat_discount_price(BigDecimal fiat_discount_price) {
        this.fiat_discount_price = fiat_discount_price;
    }

    public BigDecimal getSaving_percentage() {
        return saving_percentage;
    }

    public void setSaving_percentage(BigDecimal saving_percentage) {
        this.saving_percentage = saving_percentage;
    }

    /**
     * Format short discount message, e.g. "Save 19%".
     */
    public String formatSavingMsg(String fmt) {
        if (saving_percentage != null) {
            return String.format(fmt, saving_percentage.doubleValue());
        }
        return null;
    }
}
