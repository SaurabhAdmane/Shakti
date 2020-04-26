package com.shakticoin.app.api.kyc;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface KYCService {

    @Headers("Accept: application/json")
    @GET("userservice/v1/api/kyc/categories/")
    Call<List<KycCategory>> getKycCategories(@Header("Authorization") String authorization);

    @Multipart
    @POST("userservice/v1/api/kyc/documents/upload/")
    Call<Map<String, Object>> uploadDocument(@Header("Authorization") String authorization,
                                             @Part List<FileDescriptor> content,
                                             @Part RequestBody additionalProperties);
}
