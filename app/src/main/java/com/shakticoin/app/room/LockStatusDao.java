package com.shakticoin.app.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shakticoin.app.room.entity.LockStatus;

@Dao
public
interface LockStatusDao {

    @Query("SELECT * FROM LockStatus WHERE type_enum_id = 20 AND category_enum_id = 100")
    LockStatus getWalletKYCLockStatus();

    @Insert
    void insertAll(LockStatus... statuses);
}
