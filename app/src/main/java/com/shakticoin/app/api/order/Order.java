package com.shakticoin.app.api.order;

public class Order {
    private Long id;
    private Double amount;
    private String date_created;
    private Boolean is_completed;
    private Double total;
    private Long user;

    /*
    The response also contains tier_level that we don't need
    "tier_level": {
        "id": 5,
        "country": {
            "id": 253,
            "country": "AF",
            "available_slots": 148,
            "heat_value": 0.9866666666666667
        },
        "name": "T100",
        "short_description": "Two-year mining license",
        "price": 199,
        "is_active": false
    },
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
