package com.zobaer53.kidsdrawingapp.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BitmapDAO {
    @Query("SELECT * FROM myBitmaps")
    fun getAll(): List<BitmapsEntity>

    @Insert
    fun insertAll(myBitmapsEntity: BitmapsEntity)

    @Query("DELETE  FROM myBitmaps")
     fun delete()
}