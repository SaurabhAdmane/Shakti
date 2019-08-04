package org.shakticoin.api.payment;

import androidx.annotation.NonNull;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.util.Debug;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentRepository {
    private StripePaymentService stripeService;

    public PaymentRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stripeService = retrofit.create(StripePaymentService.class);
    }

    public void makeStripePayment(long orderId, String token, OnCompleteListener<Void> listener) {
        Call<ResponseBody> call = stripeService.makePayment(
                Session.getAuthorizationHeader(), new StripePaymentRequest(orderId, token));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        // real response data are not important
                        if (listener != null) listener.onComplete(null, null);
                    } else {
                        Debug.logErrorResponse(response);
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }
}
