package org.shakticoin.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.shakticoin.room.entity.Geo;

import java.util.List;

@Dao
public interface GeoDao {
    /** Workaround. See comments in the usage. */
    @Query("SELECT * FROM Geo WHERE id = 0")
    Geo getEmpty();

    @Query("SELECT * FROM Geo WHERE geo_type = 'COUNTRY' ORDER BY geo_name")
    List<Geo> getCountries();

    @Insert
    void insertAll(Geo... geos);
}
