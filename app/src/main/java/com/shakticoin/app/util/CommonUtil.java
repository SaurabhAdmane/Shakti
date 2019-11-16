package com.shakticoin.app.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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

    public static <K, V> Map<String, V> toMap(Object... data) {
        if (data.length == 1 && data[0] instanceof Map) {
            return checkMap(data[0]);
        }
        if (data.length % 2 == 1) {
            IllegalArgumentException e = new IllegalArgumentException("You must pass an even sized array.");
            Debug.logDebug(e.getMessage());
            throw e;
        }
        Map<String, V> map = new HashMap<String, V>();
        for (int i = 0; i < data.length;) {
            map.put((String) data[i++], (V) data[i++]);
        }
        return map;
    }

    public static <K, V> Map<K, V> checkMap(Object object) {
        if (object != null && !(object instanceof Map)) throw new ClassCastException("Not a map");
        return (Map<K, V>) object;
    }

}