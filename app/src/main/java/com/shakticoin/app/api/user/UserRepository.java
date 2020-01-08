package com.shakticoin.app.api.user;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;


public class UserRepository {
    private UserService userService;

    public UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    /**
     * Adds a new user.
     */
    public void createUser(CreateUserParameters parameters, @NonNull OnCompleteListener<User> listener) {
        Call<User> call = userService.createUser(parameters);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        User user = response.body();
                        if (user != null) {
                            Session.setUser(user);
                        }
                        listener.onComplete(null,null);
                    } else {
                        try {
                            // TODO: we need an universal way to parce error responsed
                            // currently it can be
                            // {"email":["This email already exists."],"profile":{"mobile":["This mobile number already exists."]}}
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                                String errorResponse = errorBody.string();
                                Debug.logDebug(errorResponse);
                                if (!TextUtils.isEmpty(errorResponse)) {
                                    JSONObject json = new JSONObject(errorResponse);
                                    if (json.has("email")) {
                                        JSONArray emailMessages = json.getJSONArray("email");
                                        listener.onComplete(null, new RemoteException("email", emailMessages.getString(0), response.code()));
                                        return;
                                    }
                                    if (json.has("profile")) {
                                        JSONObject jsonProfile = json.getJSONObject("profile");
                                        Iterator<String> iter = jsonProfile.keys();
                                        if (iter.hasNext()) {
                                            String name = iter.next();
                                            JSONArray messages = jsonProfile.getJSONArray(name);
                                            listener.onComplete(null,
                                                    new RemoteException(name, messages.getString(0), response.code()));
                                            return;
                                        }
                                    }
                                }
                            }

                        } catch (IOException | JSONException e) {
                            Debug.logException(e);
                        }
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });

    }

    /**
     * Retrieve a user by ID
     */
    public void getUserInfo(Integer userId, OnCompleteListener<User> listener) {
        Call<User> call = userService.getUserByID(Session.getAuthorizationHeader(), Session.getLanguageHeader(), userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        User user = response.body();
                        if (user != null) {
                            Session.setUser(user);
                            if (listener != null) listener.onComplete(user, null);
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        if (listener != null) listener.onComplete(null,
                                new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }

    /**
     * Retrieve a user by authorization token
     */
    public void getUserInfo(OnCompleteListener<User> listener) {
        userService.getUser(Session.getAuthorizationHeader(), Session.getLanguageHeader()).enqueue(new Callback<User>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        Session.setUser(user);
                        if (listener != null) listener.onComplete(user, null);
                    }
                } else {
                    Debug.logErrorResponse(response);
                    if (listener != null) listener.onComplete(null,
                            new RemoteException(response.message(), response.code()));
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<User> call, Throwable t) {
               Debug.logException(t);
               if (listener != null) listener.onComplete(null, t);
            }
        });
    }

    /**
     * Activates a new user.
     * @param userId User ID
     * @param confirmatonToken A token from the link in email confirmation message
     */
    public void acvivateUser(@NonNull Integer userId, @NonNull String confirmatonToken, OnCompleteListener<Void> listener) {

        UserActivateParameters parameters = new UserActivateParameters();
        parameters.setToken(confirmatonToken);

        userService.activateUser(userId, parameters).enqueue(new Callback<Void>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (listener != null) listener.onComplete(null, null);
                } else {
                    Debug.logErrorResponse(response);
                    if (listener != null) {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Debug.logException(t);
                if (listener != null) listener.onComplete(null, t);
            }
        });
    }

    /**
     * Reset user password
     */
    public void resetPassword(String emailAddress, @NonNull OnCompleteListener<Void> listener) {
        ResetPasswordParameters parameters = new ResetPasswordParameters();
        parameters.setEmail(emailAddress);

        userService.resetPassword(parameters).enqueue(new Callback<Void>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(null, null);
                } else {
                    Debug.logErrorResponse(response);
                    listener.onComplete(null, new RemoteException(response.message(), response.code()));
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
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
