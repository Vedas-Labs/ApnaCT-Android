package com.vedas.apna.Gallery.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vedas.apna.Gallery.Model.GalleryModel
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Gallery.View.ImageFFActivity
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.ServerApiCollection


class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var galleryArray: ArrayList<GalleryModel>
    private lateinit var imagesArray: ArrayList<ImagesModel>
    lateinit var from: String
    private var images: ArrayList<String> = ArrayList()

    fun GalleryAdapter(context: Context, galleryArray: ArrayList<GalleryModel>, imagesArray: ArrayList<ImagesModel>, images: ArrayList<String>, from: String) {
        this.context = context
        this.galleryArray = galleryArray
        this.imagesArray = imagesArray
        this.from = from
        this.images = images
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, null)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (from == "notifications"){
            Log.e("item2", ":::" + ServerApiCollection.BASE_URL1 + (images[position].substring(1)))
            Picasso.get()
                    .load(ServerApiCollection.BASE_URL1 + images[position].substring(1))
                    .error(R.drawable.image_3)
                    .placeholder(R.drawable.image_3)
                    .into(holder.img)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ImageFFActivity::class.java)
                val args = Bundle()
                args.putStringArrayList("ARRAYLIST", images)
                intent.putExtra("BUNDLE", args)
                intent.putExtra("cName", holder.adapterPosition)
                intent.putExtra("from", from)
                context.startActivity(intent)
            }
        }else {
            Log.e("item2", ":::" + ServerApiCollection.BASE_URL1 + imagesArray[position].imgPath.substring(1))
            Picasso.get()
                    .load(ServerApiCollection.BASE_URL1 + imagesArray[holder.adapterPosition].imgPath.substring(1))
                    .error(R.drawable.image_3)
                    .placeholder(R.drawable.image_3)
                    .into(holder.img)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ImageFFActivity::class.java)
                val args = Bundle()
                args.putSerializable("ARRAYLIST", imagesArray)
                intent.putExtra("BUNDLE", args)
                intent.putExtra("cName", holder.adapterPosition)
                intent.putExtra("from", from)
                context.startActivity(intent)
                /* context.startActivity(Intent(context, ImageFFActivity::class.java)
                    .putExtra("multipleImage",imagesArray))*/
            }
        }
    }

    override fun getItemCount(): Int {
        return if (from == "notifications"){
            images.size
        }else {
            imagesArray.size
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.imageView)

    }
}
