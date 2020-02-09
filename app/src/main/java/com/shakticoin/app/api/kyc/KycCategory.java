package com.shakticoin.app.api.kyc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class KycCategory implements Parcelable {
    private Integer id;
    private String name;
    private String description;
    private Integer order_no;
    private List<KycDocType> doc_types;

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

    public Integer getOrder_no() {
        return order_no != null ? order_no : 0;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }

    public List<KycDocType> getDoc_types() {
        return doc_types;
    }

    public void setDoc_types(List<KycDocType> doc_types) {
        this.doc_types = doc_types;
    }

    protected KycCategory(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        description = in.readString();
        order_no = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            doc_types = new ArrayList<KycDocType>();
            in.readList(doc_types, KycDocType.class.getClassLoader());
        } else {
            doc_types = null;
        }
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
        if (order_no == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(order_no);
        }
        if (doc_types == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(doc_types);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<KycCategory> CREATOR = new Parcelable.Creator<KycCategory>() {
        @Override
        public KycCategory createFromParcel(Parcel in) {
            return new KycCategory(in);
        }

        @Override
        public KycCategory[] newArray(int size) {
            return new KycCategory[size];
        }
    };
}
