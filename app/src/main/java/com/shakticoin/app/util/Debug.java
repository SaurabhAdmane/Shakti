package com.shakticoin.app.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class Debug {
    /** 0 - main network, 1 - test network */
    public static int NETWORK = 1;

    /**
     * Log exception as a debug message in debug build but post to Crashlytics in release.
     */
    public static void logException(Throwable e) {
        if (e != null) {
            if (BuildConfig.DEBUG) {
                String errMsg = e.getMessage();
                if (errMsg == null) {
                    errMsg = e.getClass().getName();
                }
                Log.d("com.shakticoin.app", errMsg, e);
            } else {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    /**
     * Log error message with debug level only in debug build.
     */
    public static void logDebug(String error) {
        if (error != null) {
            if (BuildConfig.DEBUG) {
                Log.d("com.shakticoin.app", error);
            }
        }
    }

    /** Log error body if API call was not successful */
    public static void logErrorResponse(Response response) {
        if (BuildConfig.DEBUG) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                try {
                    Debug.logDebug(errorBody.string());

                } catch (IOException e) {}
            }
        }
    }

    /**
     * Return one of the standard error messages for a network call that is failed.
     */
    public static String getFailureMsg(Context context, Throwable e) {
        if (e != null) {
            String errMsg = null;
            if (BuildConfig.DEBUG) {
                errMsg = e.getMessage();
            } else {
                if (e instanceof RemoteException) {
                    errMsg = e.getMessage();
                }
            }

            if (e instanceof java.io.IOException && !Session.isNetworkConnected()) {
                return context.getString(R.string.err_no_internet);
            }

            return (TextUtils.isEmpty(errMsg)) ? context.getString(R.string.err_unexpected) : errMsg;
        }
        return context.getString(R.string.err_unexpected);
    }
}
