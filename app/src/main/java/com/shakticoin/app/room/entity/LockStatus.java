package com.shakticoin.app.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"type_enum_id", "category_enum_id"}, foreignKeys = {@ForeignKey(
        entity = Enumeration.class, parentColumns = "enum_id", childColumns = "type_enum_id"
        ), @ForeignKey(entity = Enumeration.class, parentColumns = "enum_id",
        childColumns = "category_enum_id")})
public class LockStatus {
    public static String STATUS_LOCKED = "LOCKED";
    public static String STATUS_UNLOCKED = "UNLOCKED";

    @ColumnInfo(name = "type_enum_id")
    private long typeEnumId;

    @ColumnInfo(name = "category_enum_id")
    private long categoryEnumId;

    @ColumnInfo(name = "status")
    private String status;

    public LockStatus(long typeEnumId, long categoryEnumId, String status) {
        this.typeEnumId = typeEnumId;
        this.categoryEnumId = categoryEnumId;
        this.status = status;
    }

    public long getTypeEnumId() {
        return typeEnumId;
    }

    public void setTypeEnumId(long typeEnumId) {
        this.typeEnumId = typeEnumId;
    }

    public long getCategoryEnumId() {
        return categoryEnumId;
    }

    public void setCategoryEnumId(long categoryEnumId) {
        this.categoryEnumId = categoryEnumId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static LockStatus[] populateSeedData() {
        return new LockStatus[] {
                // lock statuses for wallet
                new LockStatus(10, 100, STATUS_LOCKED),
                new LockStatus(15, 100, STATUS_LOCKED),
                new LockStatus(20, 100, STATUS_LOCKED),
                new LockStatus(25, 100, STATUS_LOCKED),
                new LockStatus(30, 100, STATUS_LOCKED),
                new LockStatus(35, 100, STATUS_LOCKED),
                new LockStatus(40, 100, STATUS_LOCKED),
                new LockStatus(45, 100, STATUS_LOCKED),
                new LockStatus(50, 100, STATUS_LOCKED),
                // lock statuses for vault
                new LockStatus(10, 105, STATUS_LOCKED),
                new LockStatus(15, 105, STATUS_LOCKED),
                new LockStatus(20, 105, STATUS_LOCKED),
                new LockStatus(25, 105, STATUS_LOCKED),
                new LockStatus(30, 105, STATUS_LOCKED),
                new LockStatus(35, 105, STATUS_LOCKED),
                new LockStatus(40, 105, STATUS_LOCKED),
                new LockStatus(45, 105, STATUS_LOCKED),
                new LockStatus(50, 105, STATUS_LOCKED)
        };
    }
}
