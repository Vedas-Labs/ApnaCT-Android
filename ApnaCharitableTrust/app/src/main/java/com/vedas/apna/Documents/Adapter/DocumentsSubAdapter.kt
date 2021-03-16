@file:Suppress("FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName")

package com.vedas.apna.Documents.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Documents.Model.AduitReport
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.ServerApiCollection

class DocumentsSubAdapter : RecyclerView.Adapter<DocumentsSubAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var galleryArray: ArrayList<AduitReport>

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_document, null)
        return ViewHolder(layout)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("cName", " " + galleryArray[holder.adapterPosition].docName)
        holder.txt_doc.text = galleryArray[position].docName
        holder.img_doc.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.document))

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(ServerApiCollection.BASE_URL1 + galleryArray[holder.adapterPosition].docPath.substring(1))
            // intent.setDataAndType(Uri.parse(url), "*/*");
// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent)

            /*//context.startActivity(Intent(context, DocumentView::class.java).putExtra("photoURI", ServerApiCollection.BASE_URL1 + galleryArray.get(holder.getAdapterPosition()).image.substring(1)))
            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(Uri.parse(ServerApiCollection.BASE_URL1 + galleryArray.get(holder.getAdapterPosition()).docPath.substring(1)), "application/pdf")
            //pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

            //Create Viewer Intent
            val viewerIntent = Intent.createChooser(pdfIntent, "Open PDF")
            context.startActivity(viewerIntent)*/
            /*context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse(ServerApiCollection.BASE_URL1+galleryArray.get(holder.getAdapterPosition()).docPath.substring(1))));*/
        }
    }

    override fun getItemCount(): Int {
        return galleryArray.size
    }

    fun DocumentsSubAdapter(context: Context, galleryArray: ArrayList<AduitReport>) {
        this.context = context
        this.galleryArray = galleryArray
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_doc: TextView = itemView.findViewById(R.id.txt_doc)
        var img_doc: ImageView = itemView.findViewById(R.id.img_doc)
    }
}
