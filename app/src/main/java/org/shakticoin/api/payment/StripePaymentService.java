package org.shakticoin.api.payment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface StripePaymentService {

    @POST("v1/payment/stripe/")
    public Call<ResponseBody> makePayment(
            @Header("Authorization") String authorization,
            @Body StripePaymentRequest request
    );
}
