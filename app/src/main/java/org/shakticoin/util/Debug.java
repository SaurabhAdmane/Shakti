package org.shakticoin.util;

import android.util.Log;

import org.shakticoin.BuildConfig;


public class Debug {

    public static void logException(Throwable e) {
        if (e != null) {
            if (BuildConfig.DEBUG) {
                Log.d("org.shakticoin", e.getMessage());
            } else {
                // TODO: Crashlytics?
            }
        }
    }
}
