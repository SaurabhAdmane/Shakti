package com.shakticoin.app.api.vault;

import java.math.BigDecimal;

public class Bonus {
    private Integer id;
    private String description;
    private BigDecimal fiat_bonus;
    private BigDecimal sxe_bonus;
    private String expiry_date;

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

    public BigDecimal getFiat_bonus() {
        return fiat_bonus;
    }

    public void setFiat_bonus(BigDecimal fiat_bonus) {
        this.fiat_bonus = fiat_bonus;
    }

    public BigDecimal getSxe_bonus() {
        return sxe_bonus;
    }

    public void setSxe_bonus(BigDecimal sxe_bonus) {
        this.sxe_bonus = sxe_bonus;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }
}
