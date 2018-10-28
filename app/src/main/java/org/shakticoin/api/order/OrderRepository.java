package org.shakticoin.api.order;

import android.support.annotation.NonNull;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.util.Debug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderRepository {
    private OrderService orderService;

    public OrderRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        orderService = retrofit.create(OrderService.class);
    }

    /**
     * Create a order for a particular tier level (license type) entry fee.
     */
    public void createOrder(Long tierLevel, OnCompleteListener<Order> listener) {
        Call<Order> call = orderService.createOrder(Session.getAuthorizationHeader(), new CreateOrderRequest(tierLevel));
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        Order body = response.body();
                        if (listener != null) listener.onComplete(body,null);
                    } else {
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }
}
