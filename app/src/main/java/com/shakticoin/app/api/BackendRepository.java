package com.shakticoin.app.api;

import androidx.annotation.NonNull;

import com.shakticoin.app.util.Debug;

import retrofit2.Response;

public class BackendRepository {

    /**
     * Call the callback for a repository method in order to report an error.
     */
    protected void returnError(@NonNull OnCompleteListener<?> listener, @NonNull Throwable error) {
        Debug.logException(error);
        listener.onComplete(null, error);
    }

    /**
     * Call the callback for a repository method in order to report an error.
     * It can return exception of different types depending on error code.
     */
    protected void returnError(@NonNull OnCompleteListener<?> listener, @NonNull Response<?> response) {
        if (response.code() == 401) {
            listener.onComplete(null, new UnauthorizedException());
        } else {
            listener.onComplete(null, new RemoteException(response.message(), response.code()));
        }
    }
}
