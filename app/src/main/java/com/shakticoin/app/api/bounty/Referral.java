package com.shakticoin.app.api.bounty;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Map;

public class Referral implements Parcelable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String referrer;
    private String media;
    private String createdDate;
    private String socialMedia;
    private String status;

    public Referral() {}

    public Referral(String firstName, @NonNull String lastName, @NonNull String emailAddress,
                    String phone, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = emailAddress;
        this.phone = phone;
        this.status = status;
    }

    public Referral(@NonNull Map<String, Object> values) {
        id = (String) values.get("id");
        firstName = (String) values.get("firstName");
        lastName = (String) values.get("lastName");
        email = (String) values.get("email");
        phone = (String) values.get("phone");
        referrer = (String) values.get("referrer");
        media = (String) values.get("media");
        createdDate = (String) values.get("createdDate");
        socialMedia = (String) values.get("socialMedia");
        status = (String) values.get("status");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

        if (getId() != null ? !getId().equals(referral.getId()) : referral.getId() != null)
            return false;
        if (!getFirstName().equals(referral.getFirstName())) return false;
        if (!getLastName().equals(referral.getLastName())) return false;
        if (!getEmail().equals(referral.getEmail())) return false;
        if (getPhone() != null ? !getPhone().equals(referral.getPhone()) : referral.getPhone() != null)
            return false;
        if (!getReferrer().equals(referral.getReferrer())) return false;
        if (getMedia() != null ? !getMedia().equals(referral.getMedia()) : referral.getMedia() != null)
            return false;
        if (getCreatedDate() != null ? !getCreatedDate().equals(referral.getCreatedDate()) : referral.getCreatedDate() != null)
            return false;
        if (getSocialMedia() != null ? !getSocialMedia().equals(referral.getSocialMedia()) : referral.getSocialMedia() != null)
            return false;
        return getStatus() != null ? getStatus().equals(referral.getStatus()) : referral.getStatus() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + getReferrer().hashCode();
        result = 31 * result + (getMedia() != null ? getMedia().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        result = 31 * result + (getSocialMedia() != null ? getSocialMedia().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }

    protected Referral(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        referrer = in.readString();
        media = in.readString();
        createdDate = in.readString();
        socialMedia = in.readString();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(referrer);
        dest.writeString(media);
        dest.writeString(createdDate);
        dest.writeString(socialMedia);
        dest.writeString(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Referral> CREATOR = new Parcelable.Creator<Referral>() {
        @Override
        public Referral createFromParcel(Parcel in) {
            return new Referral(in);
        }

        @Override
        public Referral[] newArray(int size) {
            return new Referral[size];
        }
    };
}
