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

}
