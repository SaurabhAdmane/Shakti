package com.shakticoin.app.api.vault;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Bonus implements Parcelable {
    private Integer id;
    private String description;
    private BigDecimal fiat_bonus;
    private BigDecimal sxe_bonus;
    private String expiry_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFiat_bonus() {
        return fiat_bonus;
    }

    public void setFiat_bonus(BigDecimal fiat_bonus) {
        this.fiat_bonus = fiat_bonus;
    }

    public BigDecimal getSxe_bonus() {
        return sxe_bonus;
    }

    public void setSxe_bonus(BigDecimal sxe_bonus) {
        this.sxe_bonus = sxe_bonus;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    protected Bonus(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        description = in.readString();
        fiat_bonus = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        sxe_bonus = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        expiry_date = in.readString();
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
        dest.writeString(description);
        dest.writeValue(fiat_bonus);
        dest.writeValue(sxe_bonus);
        dest.writeString(expiry_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bonus> CREATOR = new Parcelable.Creator<Bonus>() {
        @Override
        public Bonus createFromParcel(Parcel in) {
            return new Bonus(in);
        }

        @Override
        public Bonus[] newArray(int size) {
            return new Bonus[size];
        }
    };
}
