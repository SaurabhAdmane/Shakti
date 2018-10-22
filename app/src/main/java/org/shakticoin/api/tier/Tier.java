package org.shakticoin.api.tier;

import android.os.Parcel;
import android.os.Parcelable;

public class Tier implements Parcelable {
    private long id;
    private Country country;
    private String name;
    private String short_description;
    private Double price;
    private Boolean is_active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "Tier{" +
                "id=" + id +
                ", country=" + country +
                ", name='" + name + '\'' +
                ", short_description='" + short_description + '\'' +
                ", price=" + price +
                ", is_active=" + is_active +
                '}';
    }

    protected Tier(Parcel in) {
        id = in.readLong();
        country = (Country) in.readValue(Country.class.getClassLoader());
        name = in.readString();
        short_description = in.readString();
        price = in.readByte() == 0x00 ? null : in.readDouble();
        byte is_activeVal = in.readByte();
        is_active = is_activeVal == 0x02 ? null : is_activeVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(country);
        dest.writeString(name);
        dest.writeString(short_description);
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(price);
        }
        if (is_active == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (is_active ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tier> CREATOR = new Parcelable.Creator<Tier>() {
        @Override
        public Tier createFromParcel(Parcel in) {
            return new Tier(in);
        }

        @Override
        public Tier[] newArray(int size) {
            return new Tier[size];
        }
    };
}
