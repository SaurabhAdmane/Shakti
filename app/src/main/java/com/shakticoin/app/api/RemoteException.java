package com.shakticoin.app.api;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RemoteException extends Exception {
    private int responseCode = -1;
    private String field;

    @Nullable
    @Override
    public String getMessage() {
        return field == null ? super.getMessage() : (field + ": " + super.getMessage());
    }

    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public RemoteException(String field, String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
        this.field = field;
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

    public String getField() {
        return field;
    }
}
