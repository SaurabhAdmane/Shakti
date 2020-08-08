package com.shakticoin.app.api.bounty;

import android.util.SparseIntArray;

import java.math.BigDecimal;

public class EffortRate {
    public enum LeadSource {
        UNKNOWN,
        EMAIL,
        PHONE_NUMBER,
        FACEBOOK,
        INSTAGRAM,
        LINKEDIN,
        PINTEREST,
        SKYPE,
        WECHAT,
        TUMBIR,
        WK,
        TWITTER,
        OTHER
    }
    public static final int LEAD_STATUS_INFLUENCED    = 1;
    public static final int LEAD_STATUS_PROGRESSING   = 2;
    public static final int LEAD_STATUS_CONVERTED     = 3;

    private final SparseIntArray effortRate = new SparseIntArray();
    private LeadSource source = LeadSource.UNKNOWN;

    public EffortRate(String leadSource) {
        if (leadSource != null) {
            for (LeadSource enumValue : LeadSource.values()) {
                if (leadSource.equals(enumValue.name())) {
                    source = enumValue;
                    break;
                }
            }
        }
    }

    public EffortRate(String leadSource, int converted, int progressing, int influenced) {
        this(leadSource);
        setStageTotal(LEAD_STATUS_CONVERTED, converted);
        setStageTotal(LEAD_STATUS_PROGRESSING, progressing);
        setStageTotal(LEAD_STATUS_INFLUENCED, influenced);
    }

    public void setLeadSource(LeadSource leadSource) {
        source = leadSource;
    }

    public LeadSource getLeadSource() {
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
