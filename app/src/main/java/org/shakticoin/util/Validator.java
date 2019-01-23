package org.shakticoin.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;

public class Validator {

    /**
     * Returns true if a given String looks like an email address.
     * @see android.util.Patterns
     */
    public static boolean isEmail(String emailAddress) {
        return emailAddress != null && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    /**
     * Returns true if a given String might be a phone number.
     * @see android.util.Patterns
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber != null && Patterns.PHONE.matcher(phoneNumber).matches();
    }

    /**
     * Returns true if a given String might be a web address.
     * @see android.util.Patterns
     */
    public static boolean isWebAddress(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * Check format of the specified url and return true if it conforms the format used
     * for Shakti referral url.
     */
    public static boolean isShaktiReferralUrl(String url) {
        if (isWebAddress(url)) {
            Uri uri = Uri.parse(url);
            // just check if it contains proper parameter
            return !TextUtils.isEmpty(uri.getQueryParameter("referral_code"));
        }
        return false;
    }

    /**
     * Check a password strength that should be at least 8 chars length and contain digits.
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        if (!password.matches(".*\\d+.*")) return false;
        return true;
    }
}
