package com.shakticoin.app.util;

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
        // 15 digits max
        if (TextUtils.isEmpty(phoneNumber)) return false;
        if (phoneNumber.length() > 15) return false;

        // only digits
        for(int i = 0; i < phoneNumber.length(); i++){
            final char c = phoneNumber.charAt(i);
            if (!Character.isDigit(c)) return false;
        }

        //TODO: the number should start from a country code
        // Database has PhoneCountryCode table with known country codes and they should be used
        // either to validate users input or filling separate drop-down with countries (as a part
        // of phone number)
        return true;
    }

    /**
     * Combines checking value being either email address or phone number.
     */
    public static boolean isEmailOrPhoneNumber(String value) {
        return isPhoneNumber(value) || isEmail(value);
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
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasOther = false;
        Character[] prevChars = new Character[] {null, null};
        for (int i = 0; i < password.length(); i++) {
            Character c = password.charAt(i);

            if (!hasDigit && Character.isDigit(c)) hasDigit = true;
            if (!hasLetter && Character.isLetter(c)) hasLetter = true;
            if (!hasOther && !Character.isLetter(c) && !Character.isDigit(c)) hasOther = true;

            // only 2 consecutive chars allowed
            if (c.equals(prevChars[0]) && c.equals(prevChars[1])) return false;

            // shift previous chars
            prevChars[0] = prevChars[1];
            prevChars[1] = c;
        }
        return hasDigit && hasLetter && hasOther;
    }

    /**
     * Return code if the given string might be a real postal code.
     * TODO: implement postal code validation
     */
    public static boolean isPostalCodeValid(String postalCode) {
        return true;
    }
}
