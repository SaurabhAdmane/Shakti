package com.shakticoin.app.api.vault;

public class PackageFeature {
    private Integer id;
    private String feature;
    private Boolean is_active;
    private Package _package;
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

    public Package getPackage() {
        return _package;
    }

    public void setPackage(Package _package) {
        this._package = _package;
    }

    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }
}
