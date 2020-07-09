package com.shakticoin.app.api.vault;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class VaultRepository extends BackendRepository {
    private VaultService service;
    private AuthRepository authRepository;

    public VaultRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(VaultService.class);
        authRepository = new AuthRepository();
    }



    public void getVaults(@NonNull OnCompleteListener<List<VaultExtended>> listener) {
        getVaults(listener, false);
    }
    public void getVaults(@NonNull OnCompleteListener<List<VaultExtended>> listener, boolean hasRecover401) {
        service.getVaults(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<List<VaultExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<VaultExtended>> call, Response<List<VaultExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401 && !hasRecover401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getVaults(listener, true);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<VaultExtended>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getVault(Integer vaultId, @NonNull OnCompleteListener<VaultExtended> listener) {
        getVault(vaultId, listener, false);
    }
    public void getVault(Integer vaultId, @NonNull OnCompleteListener<VaultExtended> listener, boolean hasRecover401) {
        service.getVault(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId).enqueue(new Callback<VaultExtended>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<VaultExtended> call, Response<VaultExtended> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401 && !hasRecover401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getVault(vaultId, listener, true);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<VaultExtended> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getVaultPackages(@NonNull Integer vaultId, @NonNull OnCompleteListener<List<PackageExtended>> listener) {
        getVaultPackages(vaultId, listener, false);
    }
    public void getVaultPackages(@NonNull Integer vaultId, @NonNull OnCompleteListener<List<PackageExtended>> listener, boolean hasRecover401) {
        service.getVaultPackages(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId)
                .enqueue(new Callback<List<PackageExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<PackageExtended>> call, Response<List<PackageExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401 && !hasRecover401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getVaultPackages(vaultId, listener, true);
                            }
                        });
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<PackageExtended>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getVaultPackage(@NonNull Integer vaultId, @NonNull Integer packageId,
                                @NonNull OnCompleteListener<PackageExtended> listener) {
        getVaultPackage(vaultId, packageId, listener, false);
    }
    public void getVaultPackage(@NonNull Integer vaultId, @NonNull Integer packageId,
                                @NonNull OnCompleteListener<PackageExtended> listener, boolean hasRecover401) {
        service.getVaultPackage(Session.getAuthorizationHeader(), Session.getLanguageHeader(), vaultId, packageId)
                .enqueue(new Callback<PackageExtended>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<PackageExtended> call, Response<PackageExtended> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete(null, new UnauthorizedException());
                                        return;
                                    }
                                    getVaultPackage(vaultId, packageId, listener, true);
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<PackageExtended> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getPackagePlans(@NonNull Integer vaultId, @NonNull Integer packageId,
                                @NonNull OnCompleteListener<List<PackagePlanExtended>> listener) {
        getPackagePlans(vaultId, packageId, listener, false);
    }
    public void getPackagePlans(@NonNull Integer vaultId, @NonNull Integer packageId,
                         @NonNull OnCompleteListener<List<PackagePlanExtended>> listener, boolean hasRecover401) {
        service.getVaultPackagePlans(Session.getAuthorizationHeader(), Session.getLanguageHeader(),
                vaultId, packageId).enqueue(new Callback<List<PackagePlanExtended>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<PackagePlanExtended>> call, Response<List<PackagePlanExtended>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete(null, new UnauthorizedException());
                                        return;
                                    }
                                    getPackagePlans(vaultId, packageId, listener, true);
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<PackagePlanExtended>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
