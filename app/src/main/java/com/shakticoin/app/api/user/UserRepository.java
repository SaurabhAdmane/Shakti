package com.shakticoin.app.api.user;

import androidx.annotation.NonNull;

import com.shakticoin.app.util.Debug;

import org.json.JSONException;
import org.json.JSONObject;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private UserService userService;

    public UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    public void createUser(CreateUserParameters parameters, OnCompleteListener<Void> listener) {
        Call<CreateUserResponse> call = userService.createUser(parameters);
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateUserResponse> call, @NonNull Response<CreateUserResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        CreateUserResponse userData = response.body();
                        if (userData != null) {
                            User user = new User();
                            user.setId(userData.getId());
                            user.setFirst_name(userData.getFirst_name());
                            user.setLast_name(userData.getLast_name());
                            user.setEmail(userData.getEmail());
                            Session.setUser(user);
                            if (listener != null) listener.onComplete(null,null);
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        if (listener != null) listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateUserResponse> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });

    }

    public void getUserInfo(Long userId, OnCompleteListener<CreateUserResponse> listener) {
        Call<CreateUserResponse> call = userService.getUserByID(Session.getAuthorizationHeader(), userId);
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateUserResponse> call, @NonNull Response<CreateUserResponse> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        CreateUserResponse body = response.body();
                        if (body != null) {
                            User user = new User();
                            user.setId(body.getId());
                            user.setFirst_name(body.getFirst_name());
                            user.setLast_name(body.getLast_name());
                            user.setEmail(body.getEmail());
                            Session.setUser(user);
                            if (listener != null) listener.onComplete(body, null);
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateUserResponse> call, @NonNull Throwable t) {
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
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("registration_status", status);
//        } catch (JSONException e) {
//            Debug.logException(e);
//            if (listener != null) listener.onComplete(null, e);
//            return;
//        }
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Call<ResponseBody> call = userService.updateUser(
//                Session.getAuthorizationHeader(), Long.toString(Session.getUser().getId()), body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (call.isExecuted()) {
//                    if (response.isSuccessful()) {
//                        if (listener != null) listener.onComplete(null, null);
//                    } else {
//                        Debug.logErrorResponse(response);
//                        if (listener != null) listener.onComplete(null,
//                                new RemoteException(response.message(), response.code()));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Debug.logException(t);
//                if (listener != null) listener.onComplete(null, t);
//            }
//        });
    }
}
