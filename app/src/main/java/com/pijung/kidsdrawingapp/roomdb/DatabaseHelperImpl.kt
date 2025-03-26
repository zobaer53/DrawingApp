package com.pijung.kidsdrawingapp.roomdb

import androidx.room.RoomDatabase

class DatabaseHelperImpl(private val database: RoomDatabase) {
    private val bitmapsDao = (database as AppDatabase).bitmapsDao()

    suspend fun insertAll(bitmaps: BitmapsEntity) {
        bitmapsDao.insertAll(bitmaps)
    }

    suspend fun delete(bitmaps: BitmapsEntity) {
        bitmapsDao.delete(bitmaps)
    }

    suspend fun getAll(): List<BitmapsEntity> {
        return bitmapsDao.getAll()
    }
} 