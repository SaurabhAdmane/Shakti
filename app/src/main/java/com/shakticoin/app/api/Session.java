package com.shakticoin.app.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;
import android.security.keystore.KeyGenParameterSpec;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

public class Session {
    private static String accessToken;
    private static String refreshToken;
    private static String shaktiId;
    private static Long walletSessionToken;
    private static String walletPassphrase;
    private static String walletByte = "-1";

    private static boolean networkConnected = true;

    public static String getAccessToken() {
        return accessToken;
    }

    public static synchronized void setAccessToken(String token) {
        accessToken = token;
    }

    public static synchronized void setRefreshToken(String token) {
        refreshToken = token;
    }

    public static String key() {
        return accessToken;
    }

    public static String getRefreshToken() {
        if (refreshToken == null) {
            Context context = ShaktiApplication.getContext();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    KeyGenParameterSpec keyGenParams = MasterKeys.AES256_GCM_SPEC;
                    String masterKeyAlias = MasterKeys.getOrCreate(keyGenParams);
                    SharedPreferences prefs = EncryptedSharedPreferences.create(
                            "ss", masterKeyAlias, context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                    refreshToken = prefs.getString(PreferenceHelper.PREF_KEY_TOKEN, null);
                } else {
                    SharedPreferences prefs =
                            context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                    refreshToken = prefs.getString(PreferenceHelper.PREF_KEY_TOKEN, null);
                }

            } catch (IOException | GeneralSecurityException e) {
                Debug.logException(e);
            }
        }
        return refreshToken;
    }

    public static void setRefreshToken(String token, boolean rememberMe) {
        if (rememberMe) {
            Context context = ShaktiApplication.getContext();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // store refresh token in an encrypted storage in order to login automatically
                    KeyGenParameterSpec keyGenParams = MasterKeys.AES256_GCM_SPEC;
                    String masterKeyAlias = MasterKeys.getOrCreate(keyGenParams);
                    SharedPreferences prefs = EncryptedSharedPreferences.create(
                            "ss", masterKeyAlias, context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(PreferenceHelper.PREF_KEY_TOKEN, token).apply();
                } else {
                    SharedPreferences prefs =
                            context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                    prefs.edit().putString(PreferenceHelper.PREF_KEY_TOKEN, token).apply();
                }

            } catch (IOException | GeneralSecurityException e) {
                Debug.logException(e);
            }
        }

        setRefreshToken(token);
    }


    public static @NonNull String getShaktiId() {
        return shaktiId;
    }

    public static synchronized void setShaktiId(@NonNull String shaktiId) {
        Session.shaktiId = shaktiId;
    }

    /**
     * Return a wallet passphrase that user entered when the wallet was opened first time. The passphrase
     * is mandatory but there is a case when it can be empty. If local storage does not have
     * wallet bytes and create wallet call return that the wallet was already created then we clear
     * passprase. This effectively mean that if no wallet bytes and passphrase then callWallet
     * is skipped.
     */
    public static String getWalletPassphrase() {
        return walletPassphrase;
    }

    public static synchronized void setWalletPassphrase(String passphrase) {
        walletPassphrase = passphrase;
    }

    public static String getWalletBytes() {
        return walletByte;
    }

    public static synchronized void setWalletBytes(String walletBytes) {
        walletByte = walletBytes;
    }

    public static synchronized void clean() {
        accessToken         = null;
        refreshToken        = null;
        shaktiId            = null;
        walletSessionToken  = null;
        walletPassphrase    = null;
        walletByte    = null;
        Context context = ShaktiApplication.getContext();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // store refresh token in an encrypted storage in order to login automatically
                KeyGenParameterSpec keyGenParams = MasterKeys.AES256_GCM_SPEC;
                String masterKeyAlias = MasterKeys.getOrCreate(keyGenParams);
                SharedPreferences prefs = EncryptedSharedPreferences.create(
                        "ss", masterKeyAlias, context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                prefs.edit().remove(PreferenceHelper.PREF_KEY_TOKEN).apply();
            } else {
                SharedPreferences prefs =
                        context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
                prefs.edit().remove(PreferenceHelper.PREF_KEY_TOKEN).apply();
            }
        } catch (IOException | GeneralSecurityException e) {
            Debug.logException(e);
        }
    }

    public static String getAuthorizationHeader() {
        return accessToken != null ? "Bearer " + accessToken : null;
    }

    public static String getLanguageHeader() {
        StringBuilder sb = new StringBuilder();
        Context context = ShaktiApplication.getContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            LocaleList localeList = context.getResources().getConfiguration().getLocales();
            for (int i = 0; i < localeList.size(); i++) {
                Locale locale = localeList.get(i);
                if (sb.length() > 0) sb.append(", ");
                sb.append(locale.toLanguageTag());
            }
        } else {
            sb.append(context.getResources().getConfiguration().locale.toString());
        }
        return sb.toString();
    }

    public static Intent unauthorizedIntent(Context context) {
        Session.clean();
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
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

    /**
     * A Wallet API session token.
     */
    public static Long getWalletSessionToken() {
        return walletSessionToken;
    }

    /**
     * Store a Wallet API session token in the session object.
     */
    public static synchronized void setWalletSessionToken(Long sessionToken) {
        walletSessionToken = sessionToken;
    }
}
