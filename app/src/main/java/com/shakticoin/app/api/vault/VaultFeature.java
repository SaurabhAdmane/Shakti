package com.shakticoin.app.api.vault;

public class VaultFeature {
    private Integer id;
    private String feature;
    private Boolean is_active;
    private Integer vault_id;
    private Integer order_no;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Integer getVault_id() {
        return vault_id;
    }

    public void setVault_id(Integer vault_id) {
        this.vault_id = vault_id;
    }

    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }
}
