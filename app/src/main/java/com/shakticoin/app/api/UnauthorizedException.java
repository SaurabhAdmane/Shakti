package com.shakticoin.app.api;

public class UnauthorizedException extends RemoteException {
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, int responseCode) {
        super(message, responseCode);
    }

    public UnauthorizedException(String field, String message, int responseCode) {
        super(field, message, responseCode);
    }
}
