package org.shakticoin.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import org.shakticoin.room.entity.PhoneCountryCode;

@Dao
public interface PhoneCountryCodeDao {

    @Insert
    void insertAll(PhoneCountryCode... countryCodes);
}
