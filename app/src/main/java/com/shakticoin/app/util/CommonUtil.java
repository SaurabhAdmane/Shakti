package com.shakticoin.app.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.license.SubscribedLicenseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil {
    /**
     * <p>It prefix a key with application package name.<br>
     * Typically should be used to prefix extra data keys with intents.</p>
     */
    public static String prefixed(String extraKey) {
        Context context = ShaktiApplication.getContext();
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

    /**
     * Return the first active subscription from the list.
     */
    public static SubscribedLicenseModel getActiveSubscription(List<SubscribedLicenseModel> subscriptions) {
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (SubscribedLicenseModel subscription : subscriptions) {
                String action = subscription.getAction();
                if (!(SubscribedLicenseModel.ACTION_EXPIRED.equals(action) || SubscribedLicenseModel.ACTION_CANCELLED.equals(action))) {
                    return subscription;
                }
            }
        }
        return null;
    }

    /**
     * Find and return device ISO country code.
     */
    @Nullable
    public static String getCountryCode2(@NonNull Context context) {
        String countryCode = null;
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            countryCode = telephonyManager.getSimCountryIso();
            if ((countryCode == null || countryCode.length() != 2) && telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                countryCode = telephonyManager.getNetworkCountryIso();
            }
        }

        if (countryCode == null || countryCode.length() != 2) {
            // use primary locale as a last resort source for country information
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }

        return !TextUtils.isEmpty(countryCode) ? countryCode.toUpperCase() : null;
    }
}