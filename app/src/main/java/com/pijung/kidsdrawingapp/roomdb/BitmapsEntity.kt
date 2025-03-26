package com.pijung.kidsdrawingapp.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitmaps_table")
data class BitmapsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bitmap: String
) 