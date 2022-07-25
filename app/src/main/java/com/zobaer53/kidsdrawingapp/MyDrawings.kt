package com.zobaer53.kidsdrawingapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zobaer53.kidsdrawingapp.roomdb.AppDatabase
import com.zobaer53.kidsdrawingapp.roomdb.DatabaseHelperImpl

class MyDrawings : AppCompatActivity() {
    private var dbHelper : DatabaseHelperImpl? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_drawings)
         dbHelper = DatabaseHelperImpl(AppDatabase.getDatabase(applicationContext))

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        val layoutManager = GridLayoutManager(this, 3)

        recyclerview.layoutManager = layoutManager

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
       val b = dbHelper!!.getAll()
        for(i in b){
            data.add(ItemsViewModel(i.mBitmap))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = RecyclerViewAdapter(this,data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        //adapter.notifyDataSetChanged()

    }
}