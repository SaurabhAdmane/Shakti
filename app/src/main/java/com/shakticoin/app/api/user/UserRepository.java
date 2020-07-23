package com.shakticoin.app.api.user;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.util.Debug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;


public class UserRepository extends BackendRepository {
    private UserService userService;
    private AuthRepository authRepository;

    public UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.USERSERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        authRepository = new AuthRepository();
    }

    /**
     * Retrieve a user by authorization token
     */
    public void getUserAccount(@NonNull OnCompleteListener<UserAccount> listener) {
        getUserAccount(listener, false);
    }
    public void getUserAccount(@NonNull OnCompleteListener<UserAccount> listener, boolean hasRecover401) {
        // FIXME: temporarily disabled as the userservice is decommissioned
        listener.onComplete(null, null);
//        userService.getUserAccount(Session.getAuthorizationHeader()).enqueue(new Callback<UserAccount>() {
//            @EverythingIsNonNull
//            @Override
//            public void onResponse(Call<UserAccount> call, Response<UserAccount> response) {
//                Debug.logDebug(response.toString());
//                if (response.isSuccessful()) {
//                    UserAccount user = response.body();
//                    if (user != null) {
//                        Session.setUserAccount(user);
//                        listener.onComplete(user, null);
//                    }
//                } else {
//                    if (response.code() == 401) {
//                        if (!hasRecover401) {
//                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
//                                @Override
//                                public void onComplete(TokenResponse value, Throwable error) {
//                                    if (error != null) {
//                                        listener.onComplete(null, new UnauthorizedException());
//                                        return;
//                                    }
//                                    getUserAccount(listener, true);
//                                }
//                            });
//                        } else {
//                            listener.onComplete(null, new UnauthorizedException());
//                        }
//                    } else {
//                        Debug.logErrorResponse(response);
//                        returnError(listener, response);
//                    }
//                }
//            }
//
//            @EverythingIsNonNull
//            @Override
//            public void onFailure(Call<UserAccount> call, Throwable t) {
//                returnError(listener, t);
//            }
//        });
    }

    /**
     * Activates a new user.
     * @param userId User ID
     * @param confirmatonToken A token from the link in email confirmation message
     */
    public void acvivateUser(@NonNull Integer userId, @NonNull String confirmatonToken, @NonNull OnCompleteListener<Void> listener) {

//        UserActivateParameters parameters = new UserActivateParameters();
//        parameters.setToken(confirmatonToken);
//
//        userService.activateUser(userId, parameters).enqueue(new Callback<Void>() {
//            @EverythingIsNonNull
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    listener.onComplete(null, null);
//                } else {
//                    Debug.logErrorResponse(response);
//                    listener.onComplete(null, new RemoteException(response.message(), response.code()));
//                }
//            }
//
//            @EverythingIsNonNull
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                returnError(listener, t);
//            }
//        });
    }

    /**
     * Reset user password
     */
    public void resetPassword(String emailAddress, @NonNull OnCompleteListener<Void> listener) {
        ResetPasswordParameters parameters = new ResetPasswordParameters(emailAddress);
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
}
