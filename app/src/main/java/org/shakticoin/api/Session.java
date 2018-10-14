package org.shakticoin.api;

import org.shakticoin.api.miner.User;

public class Session {
    private static String tokenKey;
    private static User user;

    public static String key() {
        return tokenKey;
    }

    public static synchronized void key(String key) {
        tokenKey = key;
    }

    public static User getUser() {
        return user;
    }

    public static synchronized void setUser(User user) {
        Session.user = user;
    }

    public static String getAuthorizationHeader() {
        return tokenKey != null ? "Token " + tokenKey : null;
    }
}
