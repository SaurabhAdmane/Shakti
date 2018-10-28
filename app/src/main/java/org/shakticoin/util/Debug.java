package org.shakticoin.util;

import android.content.Context;
import android.util.Log;

import org.shakticoin.BuildConfig;


public class Debug {

    /**
     * Log exception as a debug message in debug build but post to Crashlytics in release.
     */
    public static void logException(Throwable e) {
        if (e != null) {
            if (BuildConfig.DEBUG) {
                Log.d("org.shakticoin", e.getMessage());
            } else {
                // TODO: Crashlytics?
            }
        }
    }

    /**
     * Log error message with debug level only in debug build.
     */
    public static void logDebug(String error) {
        if (error != null) {
            if (BuildConfig.DEBUG) {
                Log.d("org.shakticoin", error);
            }
        }
    }

}
