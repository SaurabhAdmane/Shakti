package org.shakticoin.api;

import org.shakticoin.api.miner.User;

public class Session {
    private static String tokenKey;
    private static User user;

    private static boolean networkConnected = true;

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

    /**
     * Provide access to the flat that indicates current network availability.
     */
    public static boolean isNetworkConnected() {
        return networkConnected;
    }

    /**
     * Set or reset the network availability flag.
     */
    public static void setNetworkConnected(boolean connected) {
        networkConnected = connected;
    }
}
