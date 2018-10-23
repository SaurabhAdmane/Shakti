package org.shakticoin.api.order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderService {

    @POST("v1/mobile/order/")
    public Call<Order> createOrder(
            @Header("Authorization") String authorization,
            @Body CreateOrderRequest request);
}
