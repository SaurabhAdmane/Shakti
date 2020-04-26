package com.shakticoin.app.api.kyc;

import android.os.Parcel;
import android.os.Parcelable;

public class KycDocType implements Parcelable {
    private Integer id;
    private String name;
    private Integer order_no;
    private Integer category;

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

    public Integer getOrder_no() {
        return order_no != null ? order_no : 0;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    protected KycDocType(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        order_no = in.readByte() == 0x00 ? null : in.readInt();
        category = in.readByte() == 0x00 ? null : in.readInt();
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
        if (order_no == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(order_no);
        }
        if (category == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(category);
        }
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
