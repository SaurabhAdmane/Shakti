package com.shakticoin.app.api.common;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

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

public class CommonRepository extends BackendRepository {
    private CommonService service;
    private AuthRepository authRepository;

    public CommonRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CommonService.class);
        authRepository = new AuthRepository();
    }

    public void getRequestReasons(@NonNull OnCompleteListener<List<RequestReason>> listener) {
        getRequestReasons(listener, false);
    }
    public void getRequestReasons(@NonNull OnCompleteListener<List<RequestReason>> listener, boolean hasRecover401) {
        service.contactReasons(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<List<RequestReason>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<RequestReason>> call, Response<List<RequestReason>> response) {
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
                                    getRequestReasons(listener, true);
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
            public void onFailure(Call<List<RequestReason>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void sendSupportMessage(@NonNull ContactUs newMessage, @NonNull OnCompleteListener<ContactUs> listener) {
        sendSupportMessage(newMessage, listener, false);
    }
    public void sendSupportMessage(@NonNull ContactUs newMessage, @NonNull OnCompleteListener<ContactUs> listener, boolean hasRecover401) {
        service.sendMessage(Session.getAuthorizationHeader(), newMessage).enqueue(new Callback<ContactUs>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
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
                                    sendSupportMessage(newMessage, listener, true);
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
            public void onFailure(Call<ContactUs> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        authRepository.setLifecycleOwner(lifecycleOwner);
    }
}
