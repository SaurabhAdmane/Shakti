package com.shakticoin.app.api.payment;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentRepository extends BackendRepository {
    private StripePaymentService stripeService;

    public PaymentRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stripeService = retrofit.create(StripePaymentService.class);
    }

    public void makeStripePayment(long orderId, String token, @NonNull OnCompleteListener<Void> listener) {
        Call<ResponseBody> call = stripeService.makePayment(
                Session.getAuthorizationHeader(), new StripePaymentRequest(orderId, token));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        // real response data are not important
                        listener.onComplete(null, null);
                    } else {
                        if (response.code() == 401) {
                            listener.onComplete(null, new UnauthorizedException());
                        } else {
                            Debug.logErrorResponse(response);
                            listener.onComplete(null,
                                    new RemoteException(response.message(), response.code()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
