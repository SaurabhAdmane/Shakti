package org.shakticoin.api;

public class Session {
    private static String tokenKey;

    public static String key() {
        return tokenKey;
    }

    public static void key(String key) {
        tokenKey = key;
    }
}
