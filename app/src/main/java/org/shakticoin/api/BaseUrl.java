package org.shakticoin.api;

import org.shakticoin.BuildConfig;


/**
 * Convenience class to obtain different URL for debug and release modes.
 */
public class BaseUrl {

    public static String get() {
        final String baseUrl = "http://api1.shakticoin.com/";
        if (BuildConfig.DEBUG) {
            return baseUrl;
        } else {
            //TODO: should be something else
            return baseUrl;
        }
    }
}
