package org.shakticoin.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import org.shakticoin.R;

@Entity
public class Geo {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name="geo_type")
    private String geoType;

    @ColumnInfo(name="geo_code")
    private String geoCode;

    @ColumnInfo(name="geo_name")
    private String geoName;

    public Geo(String geoType, String geoCode, String geoName) {
        this.geoType = geoType;
        this.geoCode = geoCode;
        this.geoName = geoName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public String getGeoType() {
        return geoType;
    }

    public void setGeoType(String geoType) {
        this.geoType = geoType;
    }

    public static Geo[] populateSeedData(Context context) {
        return new Geo[] {
                new Geo("COUNTRY", "AF", context.getString(R.string.COUNTRY_AF)),
                new Geo("COUNTRY", "AX", context.getString(R.string.COUNTRY_AX)),
                new Geo("COUNTRY", "AL", context.getString(R.string.COUNTRY_AL)),
                new Geo("COUNTRY", "UA", context.getString(R.string.COUNTRY_UA))
        };
    }

    @NonNull
    @Override
    public String toString() {
        return geoName;
    }
}
