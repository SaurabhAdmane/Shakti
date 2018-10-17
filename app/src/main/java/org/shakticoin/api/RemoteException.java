package org.shakticoin.api;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.Exception;

public class RemoteException extends Exception {
    private int responseCode = -1;

    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
