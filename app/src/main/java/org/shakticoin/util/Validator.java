package org.shakticoin.util;

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
}
