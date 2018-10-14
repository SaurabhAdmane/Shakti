package org.shakticoin.api.miner;

import java.util.List;

public class MinerDataResponse {
    private User user;
    private String user_type;
    private List<Citizenship> citizenship;
    private int registration_status;
    private String referral;
    private String referral_code;
    private String referral_link;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public List<Citizenship> getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(List<Citizenship> citizenship) {
        this.citizenship = citizenship;
    }

    public int getRegistration_status() {
        return registration_status;
    }

    public void setRegistration_status(int registration_status) {
        this.registration_status = registration_status;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getReferral_link() {
        return referral_link;
    }

    public void setReferral_link(String referral_link) {
        this.referral_link = referral_link;
    }
}
