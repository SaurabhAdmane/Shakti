package com.shakticoin.app.api.miner;

import java.util.List;
import java.util.Map;

public class MinerDataResponse {
    private User user;
    private String user_type;
    private List<Citizenship> citizenship;
    private int registration_status;
    private String referral;
    private String referral_code;
    private Map<String, String> referral_link;
    private String profile_picture_url;
    private String bio;
    private String phone_number;
    private String birth_date;
    private Boolean show_bio;
    private Boolean show_location;
    private Boolean show_birth_date;
    private Boolean show_first_name;
    private String street_and_number;
    private String city;
    private String current_country;
    private String secret_question1;
    private String secret_answer1;
    private String secret_question2;
    private String secret_answer2;
    private String secret_question3;
    private String secret_answer3;
    private String secret_question4;
    private String secret_answer4;
    private String secret_question5;
    private String secret_answer5;
    // TODO: we need to know exact format for a referral
    private List<Map<String, Object>> referrals;
    private String extra_email;
    private Double wallet_available_balance;
    private Double wallet_is_active;
    private String date_joined;
    // TODO: we need to know exact format for a referral
    private List<Map<String, Object>> invitations;
    private String state;
    private String postal_code;
    private String current_country_full;
    private List<Email> emails;


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

    public Map<String, String> getReferral_link() {
        return referral_link;
    }

    public void setReferral_link(Map<String, String> referral_link) {
        this.referral_link = referral_link;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public Boolean getShow_bio() {
        return show_bio;
    }

    public void setShow_bio(Boolean show_bio) {
        this.show_bio = show_bio;
    }

    public Boolean getShow_location() {
        return show_location;
    }

    public void setShow_location(Boolean show_location) {
        this.show_location = show_location;
    }

    public Boolean getShow_birth_date() {
        return show_birth_date;
    }

    public void setShow_birth_date(Boolean show_birth_date) {
        this.show_birth_date = show_birth_date;
    }

    public Boolean getShow_first_name() {
        return show_first_name;
    }

    public void setShow_first_name(Boolean show_first_name) {
        this.show_first_name = show_first_name;
    }

    public String getStreet_and_number() {
        return street_and_number;
    }

    public void setStreet_and_number(String street_and_number) {
        this.street_and_number = street_and_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrent_country() {
        return current_country;
    }

    public void setCurrent_country(String current_country) {
        this.current_country = current_country;
    }

    public String getSecret_question1() {
        return secret_question1;
    }

    public void setSecret_question1(String secret_question1) {
        this.secret_question1 = secret_question1;
    }

    public String getSecret_answer1() {
        return secret_answer1;
    }

    public void setSecret_answer1(String secret_answer1) {
        this.secret_answer1 = secret_answer1;
    }

    public String getSecret_question2() {
        return secret_question2;
    }

    public void setSecret_question2(String secret_question2) {
        this.secret_question2 = secret_question2;
    }

    public String getSecret_answer2() {
        return secret_answer2;
    }

    public void setSecret_answer2(String secret_answer2) {
        this.secret_answer2 = secret_answer2;
    }

    public String getSecret_question3() {
        return secret_question3;
    }

    public void setSecret_question3(String secret_question3) {
        this.secret_question3 = secret_question3;
    }

    public String getSecret_answer3() {
        return secret_answer3;
    }

    public void setSecret_answer3(String secret_answer3) {
        this.secret_answer3 = secret_answer3;
    }

    public String getSecret_question4() {
        return secret_question4;
    }

    public void setSecret_question4(String secret_question4) {
        this.secret_question4 = secret_question4;
    }

    public String getSecret_answer4() {
        return secret_answer4;
    }

    public void setSecret_answer4(String secret_answer4) {
        this.secret_answer4 = secret_answer4;
    }

    public String getSecret_question5() {
        return secret_question5;
    }

    public void setSecret_question5(String secret_question5) {
        this.secret_question5 = secret_question5;
    }

    public String getSecret_answer5() {
        return secret_answer5;
    }

    public void setSecret_answer5(String secret_answer5) {
        this.secret_answer5 = secret_answer5;
    }

    public List<Map<String, Object>> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<Map<String, Object>> referrals) {
        this.referrals = referrals;
    }

    public String getExtra_email() {
        return extra_email;
    }

    public void setExtra_email(String extra_email) {
        this.extra_email = extra_email;
    }

    public Double getWallet_available_balance() {
        return wallet_available_balance;
    }

    public void setWallet_available_balance(Double wallet_available_balance) {
        this.wallet_available_balance = wallet_available_balance;
    }

    public Double getWallet_is_active() {
        return wallet_is_active;
    }

    public void setWallet_is_active(Double wallet_is_active) {
        this.wallet_is_active = wallet_is_active;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public List<Map<String, Object>> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Map<String, Object>> invitations) {
        this.invitations = invitations;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCurrent_country_full() {
        return current_country_full;
    }

    public void setCurrent_country_full(String current_country_full) {
        this.current_country_full = current_country_full;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
