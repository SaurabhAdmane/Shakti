package com.shakticoin.app.api.vault;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PackageExtended implements Parcelable {
    private Integer id;
    private String name;
    private String description;
    private List<String> features;
    private Bonus bonus;
    private Integer order_no;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }

    protected PackageExtended(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0x01) {
            features = new ArrayList<String>();
            in.readList(features, String.class.getClassLoader());
        } else {
            features = null;
        }
        bonus = (Bonus) in.readValue(Bonus.class.getClassLoader());
        order_no = in.readByte() == 0x00 ? null : in.readInt();
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
        dest.writeString(name);
        dest.writeString(description);
        if (features == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(features);
        }
        dest.writeValue(bonus);
        if (order_no == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(order_no);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PackageExtended> CREATOR = new Parcelable.Creator<PackageExtended>() {
        @Override
        public PackageExtended createFromParcel(Parcel in) {
            return new PackageExtended(in);
        }

        @Override
        public PackageExtended[] newArray(int size) {
            return new PackageExtended[size];
        }
    };
}
