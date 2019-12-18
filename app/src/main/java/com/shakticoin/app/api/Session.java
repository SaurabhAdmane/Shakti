package com.shakticoin.app.api;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.shakticoin.app.api.user.User;
import com.shakticoin.app.util.CryptoUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Session {
    private static String accessToken;
    private static String refreshToken;
    private static User user;

    private static boolean networkConnected = true;

    public static String getAccessToken() {
        return accessToken;
    }

    public static synchronized void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static synchronized void setRefreshToken(String token) {
        refreshToken = token;
    }

    public static String key() {
        return accessToken;
    }

    public static String getRefreshToken(Context context) {
        if (refreshToken == null) {
            SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
            // FIXME: temporarily we store the key un-encrypted
            refreshToken = prefs.getString(PreferenceHelper.PREF_KEY_TOKEN, null);

//            String storedToken = prefs.getString(PreferenceHelper.PREF_KEY_TOKEN, null);
//
//            if (storedToken != null) {
//                KeyPair keyPair = CryptoUtil.getMasterKeyPair(context);
//                if (keyPair != null) {
//                    try {
//                        refreshToken = CryptoUtil.decryptShortString(storedToken, keyPair);
//
//                        // user might disabled locking screen by now and we should no allow
//                        // automatic login in this case
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            KeyguardManager keyguardManager =
//                                    (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//                            if (keyguardManager != null && !keyguardManager.isDeviceSecure()) return null;
//                        }
//
//                    } catch (NoSuchPaddingException e) {
//                        Debug.logException(e);
//                        Debug.logDebug("NoSuchPaddingException: " + e.getMessage());
//                    } catch (NoSuchAlgorithmException e) {
//                        Debug.logException(e);
//                        Debug.logDebug("NoSuchAlgorithmException: " + e.getMessage());
//                    } catch (InvalidKeyException e) {
//                        Debug.logException(e);
//                        Debug.logDebug("InvalidKeyException: " + e.getMessage());
//                    } catch (BadPaddingException e) {
//                        Debug.logException(e);
//                        Debug.logDebug("BadPaddingException: " + e.getMessage());
//                    } catch (IllegalBlockSizeException e) {
//                        Debug.logException(e);
//                        Debug.logDebug("IllegalBlockSizeException: " + e.getMessage());
//                    }
//                }
//            }
        }
        return refreshToken;
    }

    public static void setRefreshToken(String token, boolean rememberMe, Context context) {
        if (rememberMe) {
//            try {
//                String encryptedKey = token;
//                KeyPair keyPair = CryptoUtil.getMasterKeyPair(context);
//                if (keyPair == null) {
//                    Debug.logDebug("No KeyPair - not encrypting");
//                } else {
//                    encryptedKey = CryptoUtil.encryptShortString(token, keyPair);
//                    SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
//                    prefs.edit().putString(PreferenceHelper.PREF_KEY_TOKEN, encryptedKey).apply();
//                }
//
//            } catch (IOException e) {
//                Debug.logException(e);
//                Debug.logDebug("IOException: " + e.getMessage());
//            } catch (NoSuchPaddingException e) {
//                Debug.logException(e);
//                Debug.logDebug("NoSuchPaddingException: " + e.getMessage());
//            } catch (InvalidKeyException e) {
//                Debug.logException(e);
//                Debug.logDebug("InvalidKeyException: " + e.getMessage());
//            } catch (BadPaddingException e) {
//                Debug.logException(e);
//                Debug.logDebug("BadPaddingException: " + e.getMessage());
//            } catch (IllegalBlockSizeException e) {
//                Debug.logException(e);
//                Debug.logDebug("IllegalBlockSizeException: " + e.getMessage());
//            } catch (NoSuchAlgorithmException e) {
//                Debug.logException(e);
//                Debug.logDebug("NoSuchAlgorithmException: " + e.getMessage());
//            }

            //FIXME: temporarily stre key unencrypted because current implementation may encrypt
            // only strings under 256 symbols while refresh token now is much longer.
            SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
            prefs.edit().putString(PreferenceHelper.PREF_KEY_TOKEN, token).apply();
        }

        setRefreshToken(token);
    }


    public static User getUser() {
        return user;
    }

    public static synchronized void setUser(User user) {
        Session.user = user;
    }

    public static synchronized void clean(Context context) {
        accessToken = null;
        refreshToken = null;
        user = null;
        SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().remove(PreferenceHelper.PREF_KEY_TOKEN).apply();
    }

    public static String getAuthorizationHeader() {
        return accessToken != null ? "Bearer " + accessToken : null;
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
