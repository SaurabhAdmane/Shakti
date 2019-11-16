package com.shakticoin.app.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.Arrays;
import java.util.List;

public class Validator {
    // source of country codes https://www.itu.int/oth/T0202.aspx?parent=T0202
    /** There are no country codes started from these numbers */
    private static List<String> emptyTwoDigitRanges = Arrays.asList("28", "80", "83", "89");
    /** List of phone country codes that are not assigned */
    private static List<String> treeDigitExclusions = Arrays.asList("210", "214", "215", "217",
            "219", "259", "292", "293", "294", "295", "296", "422", "424", "425", "426", "427",
            "428", "429", "671", "684", "693", "694", "695", "696", "697", "698", "699", "851",
            "854", "857", "858", "859", "871", "872", "873", "874", "875", "876", "877", "879",
            "884", "885", "887", "889", "967", "968", "969", "970", "978", "979", "990", "991",
            "997", "999");

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
        StringBuilder onlyDigits = new StringBuilder();
        for(int i = 0; i < phoneNumber.length(); i++){
            final char c = phoneNumber.charAt(i);
            if (Character.isDigit(c)) {
                onlyDigits.append(c);
            }
        }

        // 15 digits max
        if (TextUtils.isEmpty(onlyDigits)) return false;
        if (onlyDigits.length() > 15 || onlyDigits.length() < 7) return false;

        String codeCandidate = onlyDigits.substring(0,2);
        if (emptyTwoDigitRanges.contains(codeCandidate)) return false;
        codeCandidate = onlyDigits.substring(0, 3);
        if (treeDigitExclusions.contains(codeCandidate)) return false;

        // other combinations of leading digits are valid country codes

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
