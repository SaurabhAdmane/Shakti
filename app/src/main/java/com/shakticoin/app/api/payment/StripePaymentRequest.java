package com.shakticoin.app.api.payment;

public class StripePaymentRequest {
    private long order_id;
    private String stripe_token;

    public StripePaymentRequest() {}

    public StripePaymentRequest(long orderId, String token) {
        order_id = orderId;
        stripe_token = token;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getStripe_token() {
        return stripe_token;
    }

    public void setStripe_token(String stripe_token) {
        this.stripe_token = stripe_token;
    }
}
