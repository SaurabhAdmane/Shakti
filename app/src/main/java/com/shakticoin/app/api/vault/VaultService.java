package com.shakticoin.app.api.vault;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface VaultService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vaultservice/v1/api/vaults/")
    Call<List<VaultExtended>> getVaults(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vaultservice/v1/api/vaults/{id}")
    Call<VaultExtended> getVault(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vaultservice/v1/api/vaults/{id}/packages/")
    Call<List<PackageExtended>> getVaultPackages(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer vaultId);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vaultservice/v1/api/vaults/{id}/packages/{package_id}/")
    Call<PackageExtended> getVaultPackage(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer vaultId,
            @Path("package_id") Integer packageId);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vaultservice/v1/api/vaults/{id}/packages/{package_id}/plans/")
    Call<List<PackagePlanExtended>> getVaultPackagePlans(
            @Header("Authorization") String authorization,
            @Header("Accept-Language") String language,
            @Path("id") Integer vaultId,
            @Path("package_id") Integer packageId);
}
