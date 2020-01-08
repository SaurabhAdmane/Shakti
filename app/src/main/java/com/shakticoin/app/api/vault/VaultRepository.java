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

        service.getVaults(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<List<VaultExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<VaultExtended>> call, Response<List<VaultExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<VaultExtended> vaults = response.body();
                    listener.onComplete(vaults, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
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

    public void getVault(Integer vaultId, @NonNull OnCompleteListener<VaultExtended> listener) {
        service.getVault(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId).enqueue(new Callback<VaultExtended>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<VaultExtended> call, Response<VaultExtended> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    VaultExtended vault = response.body();
                    listener.onComplete(vault, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<VaultExtended> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    public void getVaultPackages(@NonNull Integer vaultId, @NonNull OnCompleteListener<List<PackageExtended>> listener) {
        service.getVaultPackages(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId)
                .enqueue(new Callback<List<PackageExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<PackageExtended>> call, Response<List<PackageExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<PackageExtended> packages = response.body();
                    listener.onComplete(packages, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<PackageExtended>> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    public void getVaultPackage(@NonNull Integer vaultId, @NonNull Integer packageId,
                                @NonNull OnCompleteListener<PackageExtended> listener) {
        service.getVaultPackage(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId, packageId)
                .enqueue(new Callback<PackageExtended>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<PackageExtended> call, Response<PackageExtended> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    PackageExtended pkg = response.body();
                    listener.onComplete(pkg, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<PackageExtended> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    public void getPackagePlans(@NonNull Integer vaultId, @NonNull Integer packageId,
                         @NonNull OnCompleteListener<List<PackagePlanExtended>> listener) {
        service.getVaultPackagePlans(Session.getAuthorizationHeader(), Session.getLanguageHeader(),
                vaultId, packageId).enqueue(new Callback<List<PackagePlanExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<PackagePlanExtended>> call, Response<List<PackagePlanExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<PackagePlanExtended> plans = response.body();
                    listener.onComplete(plans, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<PackagePlanExtended>> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }
}
