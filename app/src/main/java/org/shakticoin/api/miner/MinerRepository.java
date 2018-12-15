package org.shakticoin.api.miner;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.RemoteException;
import org.shakticoin.api.Session;
import org.shakticoin.util.Debug;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MinerRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private MinerService minerService;

    public MinerRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        minerService = retrofit.create(MinerService.class);
    }

    public void createUser(CreateUserRequest request, OnCompleteListener<Void> listener) {
        Call<MinerDataResponse> call = minerService.createUser(request);
        call.enqueue(new Callback<MinerDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<MinerDataResponse> call, @NonNull Response<MinerDataResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        MinerDataResponse minerData = response.body();
                        if (minerData != null) {
                            Session.setUser(minerData.getUser());
                            if (listener != null) listener.onComplete(null,null);
                        }
                    } else {
                        if (listener != null) listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MinerDataResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });

    }

    public void getUserInfo(OnCompleteListener<MinerDataResponse> listener) {
        Call<MinerDataResponse> call = minerService.getUserInfo(Session.getAuthorizationHeader());
        call.enqueue(new Callback<MinerDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<MinerDataResponse> call, @NonNull Response<MinerDataResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        MinerDataResponse body = response.body();
                        if (body != null) {
                            Session.setUser(body.getUser());
                            if (listener != null) listener.onComplete(body, null);
                        }
                    } else {
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MinerDataResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }

    /**
     * Change registration status.
     * @param status One of RegistrationStatus constants
     */
    public void updateRegistrationStatus(int status, OnCompleteListener<Void> listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registration_status", status);
        } catch (JSONException e) {
            Debug.logException(e);
            if (listener != null) listener.onComplete(null, e);
            return;
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Call<ResponseBody> call = minerService.updateMiner(
                Session.getAuthorizationHeader(), Long.toString(Session.getUser().getId()), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (call.isExecuted()) {
                    if (response.isSuccessful()) {
                        if (listener != null) listener.onComplete(null, null);
                    } else {
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }
}
