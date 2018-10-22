package org.shakticoin.api.tier;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {
    private long id;
    private String country;
    private Integer available_slots;
    private Double heat_value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAvailable_slots() {
        return available_slots;
    }

    public void setAvailable_slots(Integer available_slots) {
        this.available_slots = available_slots;
    }

    public Double getHeat_value() {
        return heat_value;
    }

    public void setHeat_value(Double heat_value) {
        this.heat_value = heat_value;
    }

    protected Country(Parcel in) {
        id = in.readLong();
        country = in.readString();
        available_slots = in.readByte() == 0x00 ? null : in.readInt();
        heat_value = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(country);
        if (available_slots == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(available_slots);
        }
        if (heat_value == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(heat_value);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
