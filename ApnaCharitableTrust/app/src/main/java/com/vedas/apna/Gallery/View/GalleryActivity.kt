package com.vedas.apna.Gallery.View

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vedas.apna.Documents.Adapter.DocumentsAdapter
import com.vedas.apna.Documents.Model.DocumentsModel
import com.vedas.apna.Gallery.Adapter.GalleryAdapter
import com.vedas.apna.Gallery.Model.GalleryModel
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Gallery.Presenter.GalleryPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks

class GalleryActivity :AppCompatActivity(), IgalleryView, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var txt_title: TextView
    private lateinit var recyclerView: RecyclerView
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var galleryArray: ArrayList<GalleryModel>
    private lateinit var imagesArray: ArrayList<ImagesModel>
    private lateinit var galleryPresenter: GalleryPresenter
    lateinit var adapter: GalleryAdapter
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
        recyclerView = findViewById(R.id.list_gallery)
        txt_title = findViewById(R.id.txt_title)
        txt_title.text = "Gallery"
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this@GalleryActivity)
        val c1 = ContextCompat.getColor(this@GalleryActivity,R.color.orange)
        val c2 = ContextCompat.getColor(this@GalleryActivity,R.color.blue)
        val c3 = ContextCompat.getColor(this@GalleryActivity,R.color.green)
        val c4 = ContextCompat.getColor(this@GalleryActivity,R.color.red)

        swipeRefreshLayout.setColorSchemeColors(c1, c2, c3, c4)
        galleryArray = ArrayList()
        imagesArray = ArrayList()
        gridLayoutManager = GridLayoutManager(this@GalleryActivity, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = GalleryAdapter()
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        galleryPresenter = GalleryPresenter()
        galleryPresenter.GalleryPresenter(this, this@GalleryActivity)
        RetrofitCallbacks.getInstace().initializeServerInterface(galleryPresenter)
        galleryPresenter.callGallery(recyclerView,adapter,true)
        adapter.GalleryAdapter(this@GalleryActivity, galleryArray, imagesArray, ArrayList(), "gallery")
        recyclerView.adapter = adapter
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

    override fun galleryresult(context: Context, galleryArray: ArrayList<GalleryModel>, imagesArray: ArrayList<ImagesModel>, recyclerView: RecyclerView, adapter: GalleryAdapter) {
        Log.e("item2",":::"+ galleryArray.size.toString()+", "+adapter)
        this.galleryArray = galleryArray
        adapter.GalleryAdapter(context, galleryArray, imagesArray, ArrayList(), "gallery")
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        if (this.swipeRefreshLayout.isRefreshing) {
            this.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun documentresult(context: Context, galleryArray: ArrayList<DocumentsModel>, recyclerView: RecyclerView, adapter: DocumentsAdapter) {
        TODO("Not yet implemented")
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        galleryPresenter.callGallery(recyclerView,adapter,false)
    }
}
