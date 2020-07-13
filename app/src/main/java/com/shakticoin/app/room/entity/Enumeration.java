package com.shakticoin.app.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = EnumerationType.class, parentColumns = "enum_type_id",
    childColumns = "enum_type_id", onDelete = SET_NULL))
public class Enumeration {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "enum_id")
    private long enumId;

    @ColumnInfo(name = "enum_type_id")
    private long enumTypeId;

    @ColumnInfo(name = "description")
    private String description;

    public Enumeration(long enumId, long enumTypeId, String description) {
        this.enumId = enumId;
        this.enumTypeId = enumTypeId;
        this.description = description;
    }

    public long getEnumId() {
        return enumId;
    }

    public void setEnumId(long enumId) {
        this.enumId = enumId;
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

    public static Enumeration[] populateSeedData() {
        return new Enumeration[] {
                // lock types
                new Enumeration(10, 20, "Status"),
                new Enumeration(15, 20, "Bonus Bounty"),
                new Enumeration(20, 20, "KYC Status"),
                new Enumeration(25, 20, "Password Recovery"),
                new Enumeration(30, 20, "Digital Nation"),
                new Enumeration(35, 20, "Date of Birth"),
                new Enumeration(40, 20, "Micro Lock"),
                new Enumeration(45, 20, "Age Lock"),
                new Enumeration(50, 20, "US Validation"),
                // lock categories
                new Enumeration(100, 10, "Wallet"),
                new Enumeration(105, 10, "Vault")
        };
    }
}
