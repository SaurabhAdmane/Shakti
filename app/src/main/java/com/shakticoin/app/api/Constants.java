package com.shakticoin.app.api;

public class Constants {

    /**
     * Possible registration status values
     */
    public class RegistrationStatus {
        /**
         * Pre-Registration
         */
        public static final int REGST_PRE = 0;
        /**
         * First step: user data
         */
        public static final int REGST_DATA = 1;
        /**
         * First step: user need verification
         */
        public static final int REGST_VERIF = 2;
        /**
         * Second step: place order
         */
        public static final int REGST_ORDER = 3;
        /**
         * Second step: payment
         */
        public static final int REGST_PMNT = 4;
        /**
         * Second step: receipt
         */
        public static final int REGST_RECPT = 5;
        /**
         * Third step: referral code and social sharing
         */
        public static final int REGST_REF = 6;
        /**
         * Third step: completed signup
         */
        public static final int REGST_COMPL = 7;
        /**
         * Second step: offline payment pending
         */
        public static final int REGST_PMNT_PENDING = 8;
        /**
         * Dashboard
         */
        public static final int REGST_DASHBRD = 9;
    }

    /**
     * "chai" is 1/100th of a coin.
     */
    public static final Integer CHAI_FACTOR     = 100;

    /**
     * "toshi" is 1/100,000th of a coin and it is the smallest
     * unit of measure in the system.
     */
    public static final Integer TOSHI_FACTOR    = 100000;

    /**
     * Default passphrase for wallet
     */
    public static final String PASSPHRASE = "empty";
}
