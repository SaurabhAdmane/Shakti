package com.shakticoin.app.api.vault;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.util.Debug;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class VaultRepository {
    private VaultService service;

    public VaultRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(VaultService.class);
    }


    public void getVaults(@NonNull OnCompleteListener<List<VaultExtended>> listener) {

        service.getVaults(Session.getAuthorizationHeader()).enqueue(new Callback<List<VaultExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<VaultExtended>> call, Response<List<VaultExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<VaultExtended> vaults = response.body();
                    listener.onComplete(vaults, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException(response.message(), response.code()));
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<VaultExtended>> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }
}
