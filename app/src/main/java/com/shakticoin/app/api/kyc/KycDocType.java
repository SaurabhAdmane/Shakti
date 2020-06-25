package com.shakticoin.app.api.kyc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class KycDocType implements Parcelable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KycDocType() {}

    public KycDocType(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    protected KycDocType(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<KycDocType> CREATOR = new Parcelable.Creator<KycDocType>() {
        @Override
        public KycDocType createFromParcel(Parcel in) {
            return new KycDocType(in);
        }

        @Override
        public KycDocType[] newArray(int size) {
            return new KycDocType[size];
        }
    };
}
