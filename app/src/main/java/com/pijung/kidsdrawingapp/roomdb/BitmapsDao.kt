package com.pijung.kidsdrawingapp.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BitmapsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bitmaps: BitmapsEntity)

    @Delete
    suspend fun delete(bitmaps: BitmapsEntity)

    @Query("SELECT * FROM bitmaps_table ORDER BY id DESC")
    suspend fun getAll(): List<BitmapsEntity>
} 