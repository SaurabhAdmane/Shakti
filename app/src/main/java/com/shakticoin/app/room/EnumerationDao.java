package com.shakticoin.app.room;

import androidx.room.Dao;
import androidx.room.Insert;

import com.shakticoin.app.room.entity.Enumeration;

@Dao
public interface EnumerationDao {

    @Insert
    void insertAll(Enumeration... enums);
}
