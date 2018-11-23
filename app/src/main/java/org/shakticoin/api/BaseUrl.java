package org.shakticoin.api;

import org.shakticoin.BuildConfig;


/**
 * Convenience class to obtain different URL for debug and release modes.
 */
public class BaseUrl {

    public static String get() {
        if (BuildConfig.DEBUG) {
            return "https://bridge1.shakticoin.com/";
        } else {
            //TODO: should be something else
            return "https://bridge1.shakticoin.com/";
        }
    }
}
