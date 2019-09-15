package com.shakticoin.app.api.referral.model;

import android.util.SparseIntArray;

import java.math.BigDecimal;

public class EffortRate {
    public static final int LEAD_STATUS_INFLUENCED    = 1;
    public static final int LEAD_STATUS_PROGRESSING   = 2;
    public static final int LEAD_STATUS_CONVERTED     = 3;

    public static final int LEAD_SOURCE_UNKNOWN       = 0;
    public static final int LEAD_SOURCE_FACEBOOK      = 1;
    public static final int LEAD_SOURCE_INSTAGRAM     = 2;
    public static final int LEAD_SOURCE_PLUS          = 3;
    public static final int LEAD_SOURCE_LINKEDIN      = 4;
    public static final int LEAD_SOURCE_TWITTER       = 5;
    public static final int LEAD_SOURCE_PINTEREST     = 6;
    public static final int LEAD_SOURCE_SKYPE         = 7;
    public static final int LEAD_SOURCE_VK            = 8;
    public static final int LEAD_SOURCE_EMAIL         = 9;
    public static final int LEAD_SOURCE_TUMBLR        = 10;
    public static final int LEAD_SOURCE_WECHAT        = 11;

    private SparseIntArray effortRate = new SparseIntArray();
    private int source = -1;

    public EffortRate(int leadSource) {
        source = leadSource;
    }

    public EffortRate(int leadSource, int converted, int progressing, int influenced) {
        this(leadSource);
        setStageTotal(LEAD_STATUS_CONVERTED, converted);
        setStageTotal(LEAD_STATUS_PROGRESSING, progressing);
        setStageTotal(LEAD_STATUS_INFLUENCED, influenced);
    }

    public void setLeadSource(int leadSource) {
        source = leadSource;
    }

    public int getLeadSource() {
        return source;
    }

    public void setStageTotal(int leadStatus, int total) {
        effortRate.put(leadStatus, total);
    }

    /**
     * Return quantity of leads with the specified status.
     */
    public int getStageTotal(int leadStatus) {
        return effortRate.get(leadStatus, 0);
    }

    /**
     * Return relative percentage of leads with the specified status.
     */
    public int getStagePercent(int leadStatus) {
        int currentTotal = effortRate.get(leadStatus, 0);

        int totalQuantity = effortRate.get(LEAD_STATUS_INFLUENCED, 0)
                + effortRate.get(LEAD_STATUS_PROGRESSING, 0)
                + effortRate.get(LEAD_STATUS_CONVERTED, 0);
        if (totalQuantity == 0) return 0;

        //calculates percentage
        BigDecimal percentage =
                BigDecimal.valueOf(currentTotal)
                        .divide(BigDecimal.valueOf(totalQuantity), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
        return percentage.intValue();
    }
}
