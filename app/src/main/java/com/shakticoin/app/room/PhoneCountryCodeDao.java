package com.shakticoin.app.room;

import androidx.room.Dao;
import androidx.room.Insert;

import com.shakticoin.app.room.entity.PhoneCountryCode;

@Dao
public interface PhoneCountryCodeDao {

    @Insert
    void insertAll(PhoneCountryCode... countryCodes);
}
