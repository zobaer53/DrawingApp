package com.zobaer53.kidsdrawingapp.roomdb

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {


    override  fun getAll(): List<BitmapsEntity> = appDatabase.bitmapDao().getAll()

    override  fun insertAll(users: BitmapsEntity) = appDatabase.bitmapDao().insertAll(users)


}