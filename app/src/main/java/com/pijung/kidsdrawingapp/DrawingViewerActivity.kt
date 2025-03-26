package com.pijung.kidsdrawingapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class DrawingViewerActivity : AppCompatActivity() {
    private lateinit var photoView: PhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_viewer)

        photoView = findViewById(R.id.photo_view)
        
        // Get the image path from intent
        val imagePath = intent.getStringExtra("IMAGE_PATH")
        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(photoView)
        } else {
            Toast.makeText(this, "Error loading drawing", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
} 