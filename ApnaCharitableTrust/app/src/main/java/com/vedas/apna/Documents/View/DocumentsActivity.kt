package com.vedas.apna.Documents.View

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vedas.apna.Documents.Adapter.DocumentsAdapter
import com.vedas.apna.Gallery.Adapter.GalleryAdapter
import com.vedas.apna.Gallery.View.IgalleryView
import com.vedas.apna.Documents.Model.DocumentsModel
import com.vedas.apna.Gallery.Model.GalleryModel
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Gallery.Presenter.GalleryPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks

class DocumentsActivity:AppCompatActivity(), IgalleryView {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var txt_title: TextView
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: DocumentsAdapter
    private lateinit var galleryArray: ArrayList<DocumentsModel>
    private lateinit var galleryPresenter: GalleryPresenter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        txt_title = findViewById(R.id.txt_title)
        recyclerView = findViewById(R.id.list_gallery)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.isEnabled = false
        txt_title.text = "Documents"
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        galleryArray = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this@DocumentsActivity)
        recyclerView.setHasFixedSize(true)
        adapter = DocumentsAdapter()
        adapter.DocumentsAdapter(this@DocumentsActivity, galleryArray)
        recyclerView.adapter = adapter
        galleryPresenter = GalleryPresenter()
        galleryPresenter.GalleryPresenter(this, this@DocumentsActivity)
        RetrofitCallbacks.getInstace().initializeServerInterface(galleryPresenter)
        galleryPresenter.callDocument(recyclerView,adapter)
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

    override fun galleryresult(context: Context, galleryArray: ArrayList<GalleryModel>, imagesArray: ArrayList<ImagesModel>, recyclerView: RecyclerView, adapter: GalleryAdapter) {
        TODO("Not yet implemented")
    }

    override fun documentresult(
            context: Context,
            galleryArray: ArrayList<DocumentsModel>,
            recyclerView: RecyclerView,
            adapter: DocumentsAdapter
    ) {
        Log.e("item2",":::"+ galleryArray.size.toString()+", "+adapter)
        this.galleryArray = galleryArray
        adapter.DocumentsAdapter(context, galleryArray)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }
}
