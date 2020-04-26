package com.shakticoin.app.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

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

    public static void setRefreshToken(String token, boolean rememberMe, Context context) {
        if (rememberMe) {
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
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
            prefs.edit().remove(PreferenceHelper.PREF_KEY_TOKEN).apply();
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
        Session.clean(context);
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
}
