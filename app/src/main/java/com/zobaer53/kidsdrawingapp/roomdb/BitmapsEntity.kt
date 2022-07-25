package com.zobaer53.kidsdrawingapp.roomdb

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "myBitmaps")
data class BitmapsEntity(
    @PrimaryKey()
    @ColumnInfo(name = "bitmapCol")
    var mBitmap: String
)

