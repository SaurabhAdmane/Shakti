package com.shakticoin.app.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EnumerationType {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "enum_type_id")
    private long enumTypeId;

    @ColumnInfo(name = "description")
    private String description;

    public EnumerationType(long enumTypeId, String description) {
        this.enumTypeId = enumTypeId;
        this.description = description;
    }

    public long getEnumTypeId() {
        return enumTypeId;
    }

    public void setEnumTypeId(long enumTypeId) {
        this.enumTypeId = enumTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static EnumerationType[] populateSeedData() {
        return new EnumerationType[] {
          new EnumerationType(10, "Lock Category (Wallet, Vault, ...)"),
          new EnumerationType(20, "Lock Type (KYC, Birthday, ...)")
        };
    }
}
