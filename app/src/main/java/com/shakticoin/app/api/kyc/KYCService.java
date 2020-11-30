package com.shakticoin.app.api.kyc;

import com.shakticoin.app.api.license.CheckoutResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface KYCService {

    /**
     * Get KYC user document types.
     */
    @GET("kyc/document-types")
    Call<ResponseBody> getDocumentTypes(@Header("Authorization") String authorization);

    /**
     * Get KYC user details.
     */
    @Headers("Accept: application/json")
    @GET("kyc/details")
    Call<KycUserView> getUserDetails(@Header("Authorization") String authorization);

    /**
     * Get all Status on Admin page
     */
    @Headers("Accept: application/json")
    @GET("kyc/status")
    Call<KycUserView> getAllStatus(@Header("Authorization") String authorization);

    /**
     * Create KYC user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("kyc/details")
    Call<Map<String, Object>> createUserDetails(@Header("Authorization") String authorization,
                                         @Body KycUserModel parameters);

    /**
     * Update KYC user.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PATCH("kyc/details")
    Call<Map<String, Object>> updateUserDetails(@Header("Authorization") String authorization,
                                         @Body KycUserModel parameters);


    /**
     * Get KYC user document types
     */
    @Headers("Accept: application/json")
    @GET("kyc/document-types")
    Call<Map<String, Object>> getKycDocTypes(@Header("Authorization") String authorization);

    /**
     * Upload KYC user's documents
     */
    @Multipart
    @POST("kyc/uploads")
    Call<Map<String, Object>> uploadDocuments(@Header("Authorization") String authorization,
                                              @Part List<MultipartBody.Part> files);

    /**
     * Purchase KYC user license.
     */
    @Headers("Accept: application/json")
    @PATCH("kyc/subscription")
    Call<CheckoutResponse> subscription(@Header("Authorization") String authorization);


    /**
     * Get KYC user document types.
     */
    @GET("kyc/wallet/")
    Call<KycUserView> getWalletRequestAPI(@Header("Authorization") String authorization);


    /**
     * Get KYC user document types.
     */
    @GET("selfyid/{walletId}")
    Call<KycUserView> getWalletByte(@Header("Authorization") String authorization, @Path("walletId") String walletId);


}
