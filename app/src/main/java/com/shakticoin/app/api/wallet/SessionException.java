package com.shakticoin.app.api.wallet;

import com.shakticoin.app.api.RemoteException;

public class SessionException extends RemoteException {

    public SessionException(String message) {
        super(message);
    }
}
