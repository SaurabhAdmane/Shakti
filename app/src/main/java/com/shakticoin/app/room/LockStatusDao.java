package com.shakticoin.app.room;

import androidx.room.Dao;
import androidx.room.Insert;

import com.shakticoin.app.room.entity.LockStatus;

@Dao
public
interface LockStatusDao {

    @Insert
    void insertAll(LockStatus... statuses);
}
