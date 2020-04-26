package com.shakticoin.app.api;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Repositories return this exception if the message can be displayed to the user.
 */
public class RemoteMessageException extends RemoteException {
    private Map<String, String> validationErrors;


    public RemoteMessageException(String message, int responseCode) {
        super(message, responseCode);
        validationErrors = new HashMap<>();
    }

    public void addValidationError(@NonNull String field, @NonNull String message) {
        validationErrors.put(field, message);
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public boolean hasValidationErrors() {
        return validationErrors.size() > 0;
    }
}
