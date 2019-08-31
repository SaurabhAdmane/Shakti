package com.shakticoin.app.util;

import android.content.Context;

public class CommonUtil {
    /**
     * <p>It prefix a key with application package name.<br>
     * Typically should be used to prefix extra data keys with intents.</p>
     */
    public static String prefixed(String extraKey, Context context) {
        if (context == null) {
            return extraKey;
        }
        return (context.getPackageName() + "." + extraKey);
    }

}
