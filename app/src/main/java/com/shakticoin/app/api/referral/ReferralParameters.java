package com.shakticoin.app.api.referral;

import android.os.Parcel;
import android.os.Parcelable;

public class ReferralParameters implements Parcelable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public ReferralParameters() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected ReferralParameters(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReferralParameters> CREATOR = new Parcelable.Creator<ReferralParameters>() {
        @Override
        public ReferralParameters createFromParcel(Parcel in) {
            return new ReferralParameters(in);
        }

        @Override
        public ReferralParameters[] newArray(int size) {
            return new ReferralParameters[size];
        }
    };
}
