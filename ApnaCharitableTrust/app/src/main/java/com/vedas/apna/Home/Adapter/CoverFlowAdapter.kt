@file:Suppress("PropertyName", "FunctionName")

package com.vedas.apna.Home.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vedas.apna.Home.Model.AboutusModel
import com.vedas.apna.R
import de.hdodenhof.circleimageview.CircleImageView

class CoverFlowAdapter : RecyclerView.Adapter<CoverFlowAdapter.ViewHolder>() {

    lateinit var activity: Context
    lateinit var data:ArrayList<AboutusModel>
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_flow_view, null)
        return ViewHolder(layout)
    }
    fun CoverFlowAdapter(context: Context, objects: ArrayList<AboutusModel>) {
        this.activity = context
        this.data = objects
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_caption.text = data[position].caption
        holder.txt_name.text = data[position].text
        Log.e("imagess","$ "+data[position].image)
        Glide.with(activity)
                .load(data[position].image)
                .into(holder.img_pic)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txt_caption: TextView = v.findViewById(R.id.txt_caption)
        var txt_name: TextView = v.findViewById(R.id.txt_name)
        var img_pic: CircleImageView = v.findViewById(R.id.img_pic)

    }
}