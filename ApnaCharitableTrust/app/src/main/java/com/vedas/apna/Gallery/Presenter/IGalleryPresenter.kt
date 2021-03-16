package com.vedas.apna.Gallery.Presenter

import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Documents.Adapter.DocumentsAdapter
import com.vedas.apna.Gallery.Adapter.GalleryAdapter

interface IGalleryPresenter {
    fun callGallery(recyclerView: RecyclerView, adapter: GalleryAdapter,progress:Boolean)
    fun callDocument(recyclerView: RecyclerView, adapter: DocumentsAdapter)
}