package com.zobaer53.kidsdrawingapp.roomdb

interface DatabaseHelper {
     fun getAll(): List<BitmapsEntity>

     fun insertAll(users: BitmapsEntity)

}