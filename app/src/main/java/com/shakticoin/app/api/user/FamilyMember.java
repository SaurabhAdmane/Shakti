package com.shakticoin.app.api.user;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMember implements Parcelable {
    private Integer id;
    private String first_name;
    private String last_name;
    private String relationship;
    private String email;
    private String phone;

    public FamilyMember() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    protected FamilyMember(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        relationship = in.readString();
        email = in.readString();
        phone = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(relationship);
        dest.writeString(email);
        dest.writeString(phone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FamilyMember> CREATOR = new Parcelable.Creator<FamilyMember>() {
        @Override
        public FamilyMember createFromParcel(Parcel in) {
            return new FamilyMember(in);
        }

        @Override
        public FamilyMember[] newArray(int size) {
            return new FamilyMember[size];
        }
    };
}
