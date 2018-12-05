package org.shakticoin.util;

import android.content.Context;
import android.util.Log;

import org.shakticoin.BuildConfig;
import org.shakticoin.R;
import org.shakticoin.api.Session;


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

    /**
     * Return one of the standard error messages for a network call that is failed.
     */
    public static String getFailureMsg(Context context, Throwable e) {
        if (e != null) {
            if (BuildConfig.DEBUG) {
                return e.getMessage();
            }

            if (e instanceof java.io.IOException && !Session.isNetworkConnected()) {
                return context.getString(R.string.err_no_internet);
            }
        }
        return context.getString(R.string.err_unexpected);
    }
}
