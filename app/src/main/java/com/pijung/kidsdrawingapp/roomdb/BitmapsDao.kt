package com.pijung.kidsdrawingapp.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BitmapsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bitmaps: BitmapsEntity)

    @Delete
    suspend fun delete(bitmaps: BitmapsEntity)

    @Query("SELECT * FROM bitmaps_table ORDER BY id DESC")
    suspend fun getAll(): List<BitmapsEntity>
} 