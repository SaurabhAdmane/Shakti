package org.shakticoin.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    public static String GENERAL_PREFERENCES = "org.shakticoin.GENERAL";

    /** Boolean preference that is true if user have passed welcome tour already */
    public static String PREF_KEY_TOUR_DONE = "tourDone";

    /** Boolean preference that is true if user has been logged in at least once */
    public static String PREF_KEY_HAS_ACCOUNT = "hasAccount";

}
