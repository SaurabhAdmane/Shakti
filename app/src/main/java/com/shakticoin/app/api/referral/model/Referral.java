package com.shakticoin.app.api.referral.model;

import androidx.annotation.NonNull;

public class Referral {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String telecomNumber;
    private String avatarUrl;
    private int status;

    public Referral() {}

    public Referral(String firstName, @NonNull String lastName, @NonNull String emailAddress,
                    String telecomNumber, int status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.telecomNumber = telecomNumber;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public @NonNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public @NonNull String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@NonNull String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelecomNumber() {
        return telecomNumber;
    }

    public void setTelecomNumber(String telecomNumber) {
        this.telecomNumber = telecomNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Return concatenation of the first (if exists) and last names.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName).append(" ");
        }
        sb.append(lastName);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Referral referral = (Referral) o;

        if (status != referral.status) return false;
        if (firstName != null ? !firstName.equals(referral.firstName) : referral.firstName != null)
            return false;
        if (!lastName.equals(referral.lastName)) return false;
        if (!emailAddress.equals(referral.emailAddress)) return false;
        if (telecomNumber != null ? !telecomNumber.equals(referral.telecomNumber) : referral.telecomNumber != null)
            return false;
        return avatarUrl != null ? avatarUrl.equals(referral.avatarUrl) : referral.avatarUrl == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + lastName.hashCode();
        result = 31 * result + emailAddress.hashCode();
        result = 31 * result + (telecomNumber != null ? telecomNumber.hashCode() : 0);
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }
}
