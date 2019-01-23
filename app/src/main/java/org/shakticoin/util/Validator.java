package org.shakticoin.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.Arrays;
import java.util.List;

public class Validator {

    /**
     * Returns true if a given String looks like an email address.
     * @see android.util.Patterns
     */
    public static boolean isEmail(String emailAddress) {
        return emailAddress != null && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    private static List<Character> allowedCharsPhone = Arrays.asList('.',' ','-','(',')');

    /**
     * Returns true if a given String might be a phone number.
     * @see android.util.Patterns
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) return false;

        // + is allowed only as a first char
        String number = phoneNumber.charAt(0) == '+' ? phoneNumber.substring(1) : phoneNumber;
        if (number.length() == 0) return false;

        // only digit allowed at first and last position
        if (!Character.isDigit(number.charAt(0))) return false;
        if (!Character.isDigit(number.charAt(number.length()-1))) return false;

        int digitsCount = 0;
        for(int i = 0; i < number.length(); i++){
            final char c = number.charAt(i);
            if (Character.isDigit(c)) {
                digitsCount++;
            } else {
                // only -, ., (, ) and space are allowed
                if (!allowedCharsPhone.contains(c)) return false;
            }
        }
        if (digitsCount < 10) return false;
        return true;
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
