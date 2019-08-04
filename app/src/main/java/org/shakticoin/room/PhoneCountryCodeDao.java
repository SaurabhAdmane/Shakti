package org.shakticoin.room;

import androidx.room.Dao;
import androidx.room.Insert;

import org.shakticoin.room.entity.PhoneCountryCode;

@Dao
public interface PhoneCountryCodeDao {

    @Insert
    void insertAll(PhoneCountryCode... countryCodes);
}
