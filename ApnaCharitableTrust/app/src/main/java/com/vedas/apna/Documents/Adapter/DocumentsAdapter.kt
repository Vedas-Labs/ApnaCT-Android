@file:Suppress("PropertyName", "PropertyName", "PropertyName", "PropertyName")

package com.vedas.apna.Documents.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Documents.View.DocumentSubAcivity
import com.vedas.apna.Documents.Model.DocumentsModel
import com.vedas.apna.R
import java.util.*


class DocumentsAdapter : RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var galleryArray: ArrayList<DocumentsModel>
    lateinit var adapter: DocumentsSubAdapter

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_document, null)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item_recycle.visibility = View.GONE
        holder.li_doc.visibility = View.VISIBLE
        if (galleryArray[position].categoryName == "Trust Documents"){
            holder.item_recycle.visibility = View.VISIBLE
            holder.li_doc.visibility = View.GONE
            holder.img_doc.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.document))
            holder.item_recycle.layoutManager = LinearLayoutManager(context)
            holder.item_recycle.setHasFixedSize(true)
            adapter = DocumentsSubAdapter()
            adapter.DocumentsSubAdapter(context, galleryArray[position].aduitReport)
            holder.item_recycle.adapter = adapter
        }else{
            holder.item_recycle.visibility = View.GONE
            holder.li_doc.visibility = View.VISIBLE
            holder.txt_doc.text = galleryArray[holder.adapterPosition].categoryName
            holder.img_doc.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.folder))
        }
        holder.itemView.setOnClickListener {
            if (galleryArray[holder.adapterPosition].categoryName != "Trust Documents"){
                val intent = Intent(context, DocumentSubAcivity::class.java)
                val args = Bundle()
                args.putSerializable("ARRAYLIST", galleryArray[holder.adapterPosition].aduitReport)
                intent.putExtra("BUNDLE", args)
                intent.putExtra("cName", galleryArray[holder.adapterPosition].categoryName)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return galleryArray.size
    }

    fun DocumentsAdapter(context: Context, galleryArray: ArrayList<DocumentsModel>) {
        this.context = context
        this.galleryArray = galleryArray
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_doc: TextView = itemView.findViewById(R.id.txt_doc)
        var item_recycle:RecyclerView = itemView.findViewById(R.id.item_recycle)
        var img_doc: ImageView = itemView.findViewById(R.id.img_doc)
        var li_doc: LinearLayout = itemView.findViewById(R.id.li_doc)
    }
}
