package com.shakticoin.app.api.common;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
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

public class CommonRepository extends BackendRepository {
    private CommonService service;
    private AuthRepository authRepository;

    public CommonRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CommonService.class);
        authRepository = new AuthRepository();
    }

    public void getRequestReasons(@NonNull OnCompleteListener<List<RequestReason>> listener) {
        service.contactReasons(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<List<RequestReason>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<RequestReason>> call, Response<List<RequestReason>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                getRequestReasons(listener);
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
            public void onFailure(Call<List<RequestReason>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void sendSupportMessage(@NonNull ContactUs newMessage, @NonNull OnCompleteListener<ContactUs> listener) {
        service.sendMessage(Session.getAuthorizationHeader(), newMessage).enqueue(new Callback<ContactUs>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401) {
                        authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                            @Override
                            public void onComplete(TokenResponse value, Throwable error) {
                                if (error != null) {
                                    listener.onComplete(null, new UnauthorizedException());
                                    return;
                                }
                                sendSupportMessage(newMessage, listener);
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
            public void onFailure(Call<ContactUs> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }
}
