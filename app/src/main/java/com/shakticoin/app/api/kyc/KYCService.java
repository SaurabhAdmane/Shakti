package com.shakticoin.app.api.kyc;

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

public interface KYCService {

    /**
     * Get KYC user document types.
     */
    @GET("kyc/document-types")
    Call<ResponseBody> getDocumentTypes(@Header("Authorization") String authorization);

    /**
     * Get KYC user details.<br/>
     * Returned data:<br/>
     * <pre>
     * {
     *     "shaktiID":"65d8841d-c995-45dc-b4ad-61ad652293de",
     *     "firstName":"Oleg",
     *     "lastName":"Andr",
     *     "middleName":null,
     *     "fullName":"Oleg Andr",
     *     "dob":"1967-05-11",
     *     "referenceEmail":null,
     *     "referenceMobile":null,
     *     "address":null,
     *     "primaryEmail":"andreyev.oleg@gmail.com",
     *     "primaryMobile":null,
     *     "secondaryEmail":null,
     *     "secondaryMobile":null,
     *     "relationshipStatus":null,
     *     "relation":null,
     *     "education1":null,
     *     "education2":null,
     *     "deviceUDID":null,
     *     "emailAlert":false,
     *     "occupation":null,
     *     "fastTrack":false,
     *     "subscriptionId":null,
     *     "paymentStatus":null,
     *     "verificationStatus":null,
     *     "verificationComments":null,
     *     "kycStatus":"LOCKED"
     * }
     * </pre>
     */
    @Headers("Accept: application/json")
    @GET("kyc/details")
    Call<Map<String, Object>> getUserDetails(@Header("Authorization") String authorization);

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
    Call<Map<String, Object>> uploadDocument(@Header("Authorization") String authorization,
                                             @Part List<MultipartBody.Part> files);
}
