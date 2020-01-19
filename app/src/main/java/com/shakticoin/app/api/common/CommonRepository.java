package com.shakticoin.app.api.common;

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

public class CommonRepository {
    CommonService service;

    public CommonRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CommonService.class);
    }

    public void getRequestReasons(@NonNull OnCompleteListener<List<RequestReason>> listener) {
        service.contactReasons(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<List<RequestReason>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<RequestReason>> call, Response<List<RequestReason>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    List<RequestReason> reasons = response.body();
                    listener.onComplete(reasons, null);
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
            public void onFailure(Call<List<RequestReason>> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
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
                    ContactUs message = response.body();
                    listener.onComplete(message, null);
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
            public void onFailure(Call<ContactUs> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }
}
