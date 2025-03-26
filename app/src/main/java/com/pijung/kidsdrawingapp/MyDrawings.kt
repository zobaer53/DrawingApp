package com.pijung.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pijung.kidsdrawingapp.roomdb.AppDatabase
import com.pijung.kidsdrawingapp.roomdb.BitmapsEntity
import com.pijung.kidsdrawingapp.roomdb.DatabaseHelperImpl
import kotlinx.coroutines.launch
import java.io.IOException

class MyDrawings : AppCompatActivity() {

    private var dbHelper: DatabaseHelperImpl? = null
    private lateinit var bitmapList: List<BitmapsEntity>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DrawingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_drawings)

        dbHelper = DatabaseHelperImpl(AppDatabase.getDatabase(applicationContext))
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DrawingAdapter(this)
        recyclerView.adapter = adapter

        loadDrawings()
    }

    private fun loadDrawings() {
        lifecycleScope.launch {
            try {
                bitmapList = dbHelper!!.getAll()
                adapter.submitList(bitmapList)
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MyDrawings,
                        "Error loading drawings: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun deleteDrawing(bitmapEntity: BitmapsEntity) {
        lifecycleScope.launch {
            try {
                dbHelper!!.delete(bitmapEntity)
                loadDrawings()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MyDrawings,
                        "Error deleting drawing: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun shareDrawing(bitmapEntity: BitmapsEntity) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(bitmapEntity.bitmap))
        startActivity(Intent.createChooser(intent, "Share Drawing"))
    }

    fun editDrawing(bitmapEntity: BitmapsEntity) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("bitmap", bitmapEntity.bitmap)
        startActivity(intent)
    }
} 