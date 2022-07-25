package com.zobaer53.kidsdrawingapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BitmapsEntity::class], version = 2)
@androidx.room.TypeConverters(TypeConverters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun bitmapDao(): BitmapDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "myDb"
            ).allowMainThreadQueries().build()
    }
}


