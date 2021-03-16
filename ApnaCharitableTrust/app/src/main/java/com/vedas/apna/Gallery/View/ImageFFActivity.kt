package com.vedas.apna.Gallery.View

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.vedas.apna.Gallery.Adapter.SlidingZoomImageVideo_Adapter
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Helper.ViewPagerFixed
import com.vedas.apna.R
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ImageFFActivity : AppCompatActivity() {
    private lateinit var rl_cancel: RelativeLayout
    lateinit var img_back: ImageView
    private lateinit var pager: ViewPagerFixed
    private var videoAdapter: SlidingZoomImageVideo_Adapter? = null
    private lateinit var imagesarray: ArrayList<String>
    private lateinit var models: ArrayList<ImagesModel>
    private var categoryName: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_ff)
        rl_cancel = findViewById(R.id.rl_cancel)
        img_back = findViewById(R.id.img_back)
        pager = findViewById(R.id.pager)
        rl_cancel.setOnClickListener(closeclick)
        img_back.setOnClickListener(closeclick)
        imagesarray = ArrayList()
        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        categoryName = intent.getIntExtra("cName", -1)
        Log.e("catgry", "::::::$categoryName")
        if (intent.getStringExtra("from").equals("notifications")){
            imagesarray = args.getStringArrayList("ARRAYLIST") as ArrayList<String>
        }else{
            @Suppress("UNCHECKED_CAST")
            models = args.getSerializable("ARRAYLIST") as ArrayList<ImagesModel>
            for (item in 0 until models.size) {
                imagesarray.add(models[item].imgPath)
            }
        }

        videoAdapter = SlidingZoomImageVideo_Adapter(this@ImageFFActivity, imagesarray)
        //pager.postDelayed(Runnable { pager.setCurrentItem(categoryName) }, 10)
        pager.adapter = videoAdapter
        //Glide.with(applicationContext).load(intent.getStringExtra("photoURI")).into(photoView)
    }
    private val closeclick = View.OnClickListener {
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        pager.currentItem = categoryName
    }
}