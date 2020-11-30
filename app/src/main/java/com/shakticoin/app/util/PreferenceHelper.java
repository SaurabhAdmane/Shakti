package com.shakticoin.app.util;

public class PreferenceHelper {
    public static final String GENERAL_PREFERENCES = "com.shakticoin.app.GENERAL";

    /** Boolean preference that is true if user have passed welcome tour already */
    public static final String PREF_KEY_TOUR_DONE = "tourDone";

    /** Boolean preference that is true if user has been logged in at least once */
    public static final String PREF_KEY_HAS_ACCOUNT = "hasAccount";

    /** Authorization token for automatic login */
    public static final String PREF_KEY_TOKEN = "t";

    /** Wallet ID */
    public static final String PREF_WALLET_BYTES = "wallet";

    /** We should display MyReferralsLockedFragment only one time and this key store information about this. */
    public static final String PREF_KEY_SHOW_UNLOCK_BOUNTY_INFO  = "showUnlockBountyInfo";

    /**
     * When a user opens a dynamic link provided by someone else it should use the promo code
     * to register the person who referred him. We store this code under this key temporarily
     * until user is ready to register his referral. Once the code is used we rewriting the value
     * with prefix _ in order to avoid another usage.
     */
    public static final String PREF_KEY_INVITATION_CODE = "referralCode";
}
