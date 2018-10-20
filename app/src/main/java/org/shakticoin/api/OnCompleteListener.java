package org.shakticoin.api;

public abstract class OnCompleteListener<T> {

    /**
     * Repository method calls this listener when API call is completed. Exception parameter
     * is null if the call was finished successfully.
     */
    public abstract void onComplete(T value, Throwable error);
}
