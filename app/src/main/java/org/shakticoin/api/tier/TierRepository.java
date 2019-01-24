package org.shakticoin.api.tier;

import android.support.annotation.NonNull;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.util.Debug;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TierRepository {
    private TierService tierService;

    public TierRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tierService = retrofit.create(TierService.class);
    }

    public void getTiers(@NonNull String countryCode, OnCompleteListener<List<Tier>> listener) {
        Call<List<Tier>> call = tierService.getTiers(Session.getAuthorizationHeader(), countryCode);
        call.enqueue(new Callback<List<Tier>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tier>> call, @NonNull Response<List<Tier>> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        List<Tier> tiers = response.body();
                        if (listener != null) listener.onComplete(tiers, null);

                    } else {
                        Debug.logErrorResponse(response);
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tier>> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }
}
