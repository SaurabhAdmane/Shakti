package org.shakticoin.api.miner;

import android.support.annotation.NonNull;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.Session;
import org.shakticoin.util.Debug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MinerRepository {
    private MinerService minerService;

    public MinerRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        minerService = retrofit.create(MinerService.class);
    };

    public void createUser(CreateUserRequest request, OnCompleteListener listener) {
        Call<MinerDataResponse> call = minerService.createUser(request);
        call.enqueue(new Callback<MinerDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<MinerDataResponse> call, @NonNull Response<MinerDataResponse> response) {
                if (call.isExecuted() && response.isSuccessful()) {
                    MinerDataResponse minerData = response.body();
                    if (minerData != null) {
                        Session.setUser(minerData.getUser());
                        if (listener != null) listener.onComplete(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MinerDataResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(t);
            }
        });

    }
}
