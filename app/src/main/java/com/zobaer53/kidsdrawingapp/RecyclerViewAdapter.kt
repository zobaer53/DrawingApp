package com.zobaer53.kidsdrawingapp


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.zobaer53.kidsdrawingapp.roomdb.AppDatabase
import com.zobaer53.kidsdrawingapp.roomdb.BitmapsEntity
import com.zobaer53.kidsdrawingapp.roomdb.DatabaseHelperImpl


class RecyclerViewAdapter(private val context:Context, private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

   val dbHelper = DatabaseHelperImpl(AppDatabase.getDatabase(context))

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        Log.i("Tag","adapter1 ${itemsViewModel.image}")
        Glide.with(context).load(itemsViewModel.image).into(holder.imageView);
        holder.imageView.setOnClickListener{
            mList.drop(position)
            notifyItemRemoved(position)
             val db = AppDatabase.getDatabase(context).bitmapDao()
            db.delete()
            for(i in mList) {
                val mBitmap = BitmapsEntity(i.image)
                dbHelper.insertAll(mBitmap)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


    }
}
