package com.shakticoin.app.room;

import androidx.room.Dao;
import androidx.room.Insert;

import com.shakticoin.app.room.entity.EnumerationType;

@Dao
public interface EnumerationTypeDao {

    @Insert
    void insertAll(EnumerationType... enumTypes);
}
