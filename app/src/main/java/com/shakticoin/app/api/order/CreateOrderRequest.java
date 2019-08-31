package com.shakticoin.app.api.order;

public class CreateOrderRequest {
    private Long tier_level;

    public CreateOrderRequest(Long tierLevel) {
        tier_level = tierLevel;
    }

    public Long getTier_level() {
        return tier_level;
    }

    public void setTier_level(Long tier_level) {
        this.tier_level = tier_level;
    }
}
