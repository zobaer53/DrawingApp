package com.pijung.kidsdrawingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pijung.kidsdrawingapp.roomdb.BitmapsEntity

class DrawingAdapter(private val activity: MyDrawings) : ListAdapter<BitmapsEntity, DrawingAdapter.DrawingViewHolder>(DrawingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drawing, parent, false)
        return DrawingViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrawingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DrawingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_view)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)
        private val shareButton: ImageView = itemView.findViewById(R.id.share_button)
        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)

        fun bind(bitmapEntity: BitmapsEntity) {
            Glide.with(itemView.context)
                .load(bitmapEntity.bitmap)
                .into(imageView)

            deleteButton.setOnClickListener {
                activity.deleteDrawing(bitmapEntity)
            }

            shareButton.setOnClickListener {
                activity.shareDrawing(bitmapEntity)
            }

            editButton.setOnClickListener {
                try {
                    activity.editDrawing(bitmapEntity)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private class DrawingDiffCallback : DiffUtil.ItemCallback<BitmapsEntity>() {
        override fun areItemsTheSame(oldItem: BitmapsEntity, newItem: BitmapsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BitmapsEntity, newItem: BitmapsEntity): Boolean {
            return oldItem == newItem
        }
    }
} 