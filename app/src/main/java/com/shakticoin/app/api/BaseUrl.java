package com.shakticoin.app.api;

import com.shakticoin.app.BuildConfig;


/**
 * Convenience class to obtain different URL for debug and release modes.
 */
public class BaseUrl {
    private static final String BASE_URL_DEBUG = "https://dev-api.shakticoin.com/";
    private static final String BASE_URL = BASE_URL_DEBUG; // TODO: must be something different

    public static String get() {
        if (BuildConfig.DEBUG) {
            return BASE_URL_DEBUG;
        } else {
            return BASE_URL;
        }
    }
}
