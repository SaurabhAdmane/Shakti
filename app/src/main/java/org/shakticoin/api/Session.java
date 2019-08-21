package org.shakticoin.api;

import android.content.Context;
import android.content.SharedPreferences;

import org.shakticoin.api.miner.User;
import org.shakticoin.util.CryptoUtil;
import org.shakticoin.util.Debug;
import org.shakticoin.util.PreferenceHelper;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Session {
    private static String tokenKey;
    private static User user;

    private static boolean networkConnected = true;

    public static String key() {
        return tokenKey;
    }

    public static String key(Context context) {
        if (tokenKey == null) {
            SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
            String storedToken = prefs.getString(PreferenceHelper.PREF_KEY_TOKEN, null);

            if (storedToken != null) {
                KeyPair keyPair = CryptoUtil.getMasterKeyPair(context);
                if (keyPair != null) {
                    try {
                        tokenKey = CryptoUtil.decryptShortString(storedToken, keyPair);

                    } catch (NoSuchPaddingException e) {
                        Debug.logException(e);
                        Debug.logDebug("NoSuchPaddingException: " + e.getMessage());
                    } catch (NoSuchAlgorithmException e) {
                        Debug.logException(e);
                        Debug.logDebug("NoSuchAlgorithmException: " + e.getMessage());
                    } catch (InvalidKeyException e) {
                        Debug.logException(e);
                        Debug.logDebug("InvalidKeyException: " + e.getMessage());
                    } catch (BadPaddingException e) {
                        Debug.logException(e);
                        Debug.logDebug("BadPaddingException: " + e.getMessage());
                    } catch (IllegalBlockSizeException e) {
                        Debug.logException(e);
                        Debug.logDebug("IllegalBlockSizeException: " + e.getMessage());
                    }
                }
            }
        }
        return tokenKey;
    }

    public static synchronized void key(String key) {
        tokenKey = key;
    }

    public static void key(String key, boolean rememberMe, Context context) {
        if (rememberMe) {
            try {
                String encryptedKey = key;
                KeyPair keyPair = CryptoUtil.getMasterKeyPair(context);
                if (keyPair == null) {
                    Debug.logDebug("No KeyPair - not encrypting");
                } else {
                    encryptedKey = CryptoUtil.encryptShortString(key, keyPair);
                    SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                    prefs.edit().putString(PreferenceHelper.PREF_KEY_TOKEN, encryptedKey).apply();
                }

            } catch (IOException e) {
                Debug.logException(e);
                Debug.logDebug("IOException: " + e.getMessage());
            } catch (NoSuchPaddingException e) {
                Debug.logException(e);
                Debug.logDebug("NoSuchPaddingException: " + e.getMessage());
            } catch (InvalidKeyException e) {
                Debug.logException(e);
                Debug.logDebug("InvalidKeyException: " + e.getMessage());
            } catch (BadPaddingException e) {
                Debug.logException(e);
                Debug.logDebug("BadPaddingException: " + e.getMessage());
            } catch (IllegalBlockSizeException e) {
                Debug.logException(e);
                Debug.logDebug("IllegalBlockSizeException: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                Debug.logException(e);
                Debug.logDebug("NoSuchAlgorithmException: " + e.getMessage());
            }
        }

        key(key);
    }


    public static User getUser() {
        return user;
    }

    public static synchronized void setUser(User user) {
        Session.user = user;
    }

    public static synchronized void clean(Context context) {
        tokenKey = null;
        user = user;
        SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().remove(PreferenceHelper.PREF_KEY_TOKEN).apply();
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
