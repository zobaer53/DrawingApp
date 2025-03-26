package com.pijung.kidsdrawingapp.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BitmapsDao {
    @Insert
    suspend fun insertAll(bitmaps: BitmapsEntity)

    @Delete
    suspend fun delete(bitmaps: BitmapsEntity)

    @Query("SELECT * FROM bitmaps_table")
    fun getAll(): List<BitmapsEntity>
} 