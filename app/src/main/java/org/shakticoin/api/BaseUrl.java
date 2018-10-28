package org.shakticoin.api;

import org.shakticoin.BuildConfig;


/**
 * Convenience class to obtain different URL for debug and release modes.
 */
public class BaseUrl {

    public static String get() {
        if (BuildConfig.DEBUG) {
            return "https://shaktiusers.djangowebstudio.com/";
//            return "https://stg.shakticoin.com/";
        } else {
            //TODO: should be something else
            return "https://shaktiusers.djangowebstudio.com/";
//            return "https://stg.shakticoin.com/";
        }
    }
}
