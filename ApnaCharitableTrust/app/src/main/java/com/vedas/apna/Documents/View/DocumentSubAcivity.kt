package com.vedas.apna.Documents.View

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vedas.apna.Documents.Adapter.DocumentsSubAdapter
import com.vedas.apna.Documents.Model.AduitReport
import com.vedas.apna.R


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DocumentSubAcivity :AppCompatActivity() {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var txt_title: TextView
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: DocumentsSubAdapter
    private var aduitReport: ArrayList<AduitReport> = ArrayList()
    private var categoryName: String = ""
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        aduitReport = args.getSerializable("ARRAYLIST") as ArrayList<AduitReport>
        categoryName = intent.getStringExtra("cName")
        init()
    }
    private fun init() {
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        txt_title = findViewById(R.id.txt_title)
        recyclerView = findViewById(R.id.list_gallery)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = false
        txt_title.text = this.categoryName

        recyclerView.layoutManager = LinearLayoutManager(this@DocumentSubAcivity)
        recyclerView.setHasFixedSize(true)
        adapter = DocumentsSubAdapter()
        adapter.DocumentsSubAdapter(this@DocumentSubAcivity, this.aduitReport)
        recyclerView.adapter = adapter

        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

}
