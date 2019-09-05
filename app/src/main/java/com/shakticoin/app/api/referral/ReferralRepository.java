package com.shakticoin.app.api.referral;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.referral.model.EffortRate;

import java.util.ArrayList;
import java.util.List;

public class ReferralRepository {

    public void getEffortRates(OnCompleteListener<List<EffortRate>> listener) {
        // TODO: return real data when API is ready

        // mockup data
        ArrayList<EffortRate> resultList = new ArrayList<>();
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_FACEBOOK, 64, 20, 16));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_INSTAGRAM, 34, 29, 37));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_PLUS, 34, 29, 37));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_LINKEDIN, 20, 34, 46));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_TWITTER, 0, 42, 58));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_PINTEREST, 20, 50, 30));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_SKYPE, 20, 20, 20)); // not 100, error for testing
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_VK, 20, 50, 30));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_WECHAT, 20, 50, 30));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_TUMBLR, 20, 50, 30));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_EMAIL, 20, 50, 30));
        resultList.add(new EffortRate(EffortRate.LEAD_SOURCE_UNKNOWN, 3, 97, 0));

        listener.onComplete(resultList, null);
    }
}
