package com.pijung.kidsdrawingapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pijung.kidsdrawingapp.roomdb.AppDatabase
import com.pijung.kidsdrawingapp.roomdb.BitmapsEntity
import com.pijung.kidsdrawingapp.roomdb.DatabaseHelperImpl
import kotlinx.coroutines.launch
import java.io.File

class MyDrawings : AppCompatActivity() {

    private var dbHelper: DatabaseHelperImpl? = null
    private lateinit var bitmapList: List<BitmapsEntity>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DrawingAdapter
    private var customProgressDialog: Dialog? = null

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
        showProgressDialog()
        lifecycleScope.launch {
            try {
                bitmapList = dbHelper!!.getAll()
                runOnUiThread {
                    cancelProgressDialog()
                    adapter.submitList(bitmapList)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    cancelProgressDialog()
                    Toast.makeText(this@MyDrawings, "Error loading drawings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showEditOptions(bitmapEntity: BitmapsEntity) {
        val options = arrayOf("Edit Drawing", "Delete Drawing")
        AlertDialog.Builder(this)
            .setTitle("Drawing Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editDrawing(bitmapEntity)
                    1 -> deleteDrawing(bitmapEntity)
                }
            }
            .show()
    }

    fun editDrawing(bitmapEntity: BitmapsEntity) {
        try {
            val file = File(bitmapEntity.bitmap)
            if (file.exists()) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("EDIT_MODE", true)
                    putExtra("IMAGE_PATH", bitmapEntity.bitmap)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Drawing file not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error opening drawing: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteDrawing(bitmapEntity: BitmapsEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete Drawing")
            .setMessage("Are you sure you want to delete this drawing?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    try {
                        // Delete the file from storage
                        val file = File(bitmapEntity.bitmap)
                        if (file.exists()) {
                            file.delete()
                        }
                        
                        // Delete from database
                        dbHelper!!.delete(bitmapEntity)
                        runOnUiThread {
                            Toast.makeText(this@MyDrawings, "Drawing deleted successfully!", Toast.LENGTH_SHORT).show()
                            loadDrawings() // Reload the list
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@MyDrawings, "Error deleting drawing: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this@MyDrawings)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    fun shareDrawing(bitmapEntity: BitmapsEntity) {
        try {
            val file = File(bitmapEntity.bitmap)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap))
                }
                startActivity(Intent.createChooser(shareIntent, "Share Drawing"))
            } else {
                Toast.makeText(this, "Drawing file not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error sharing drawing: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = android.provider.MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Drawing_${System.currentTimeMillis()}",
            "Drawing from Kids Drawing App"
        )
        return Uri.parse(path)
    }
} 
