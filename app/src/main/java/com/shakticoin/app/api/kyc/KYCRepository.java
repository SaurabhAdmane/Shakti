package com.shakticoin.app.api.kyc;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class KYCRepository extends BackendRepository {
    private KYCService service;
    private AuthRepository authRepository;

    public KYCRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(KYCService.class);
        authRepository = new AuthRepository();
    }

    /**
     * Retrieve KYC information.
     */
    public void getKycCategories(OnCompleteListener<List<KycCategory>> listener) {
        service.getKycCategories(Session.getAuthorizationHeader()).enqueue(new Callback<List<KycCategory>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<KycCategory>> call, Response<List<KycCategory>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<KycCategory> categories = response.body();
                    if (categories != null && categories.size() > 0) {
                        Collections.sort(categories,
                                (o1, o2) -> o1.getOrder_no().compareTo(o2.getOrder_no()));
                    }
                    listener.onComplete(categories, null);
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getKycCategories(listener);
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
            public void onFailure(Call<List<KycCategory>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
