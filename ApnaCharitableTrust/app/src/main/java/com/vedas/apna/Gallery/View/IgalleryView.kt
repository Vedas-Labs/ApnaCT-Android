package com.vedas.apna.Gallery.View

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Gallery.Adapter.GalleryAdapter
import com.vedas.apna.Documents.Model.DocumentsModel
import com.vedas.apna.Gallery.Model.GalleryModel
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Documents.Adapter.DocumentsAdapter

interface IgalleryView {
    fun galleryresult(
            context: Context,
            galleryArray: ArrayList<GalleryModel>,
            imagesArray: ArrayList<ImagesModel>,
            recyclerView: RecyclerView,
            adapter: GalleryAdapter
    )
    fun documentresult(
            context: Context,
            galleryArray: ArrayList<DocumentsModel>,
            recyclerView: RecyclerView,
            adapter: DocumentsAdapter
    )
    fun ToastMsg(toastmsg: String)
}