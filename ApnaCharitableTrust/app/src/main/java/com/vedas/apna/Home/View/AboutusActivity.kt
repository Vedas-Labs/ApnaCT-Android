@file:Suppress("PrivatePropertyName")

package com.vedas.apna.Home.View

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vedas.apna.Home.Adapter.CoverFlowAdapter
import com.vedas.apna.Home.Model.AboutusModel
import com.vedas.apna.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList


class AboutusActivity : AppCompatActivity(){
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var txt_chair_msg: TextView
    lateinit var adapter: CoverFlowAdapter
    private lateinit var aboutUsModel: ArrayList<AboutusModel>
    private lateinit var chariman:CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)
        settingDummyData()
        init()
    }

    private fun settingDummyData() {
        aboutUsModel = ArrayList()
        aboutUsModel.add(AboutusModel(R.drawable.sudharani, "Smt. Y.Sudha Rani", "Secretary"))
        aboutUsModel.add(AboutusModel(R.drawable.marysunitha, "Smt. P.Mary Aruna", "Treasurer"))
        aboutUsModel.add(AboutusModel(R.drawable.balavinod, "Sri Y.Bala Vinod Kumar Reddy", "Trustee"))
        aboutUsModel.add(AboutusModel(R.drawable.maryanitha, "Smt P.Mary Sunitha Rani", "Trustee"))
    }

    private fun init() {
        val sourceString = "I"+"<b>" + " Mr. P Anil Kumar Reddy" + "</b> " + " is very proud to say that i served in Indian Army for 17 years. I would like to serve the society in my future. I have established a trust in the name of"+"<b>" + " Apna Charitable Trust “Joy of Giving “ " + "</b>"+ ". In the year 13th Oct 2015. The main purpose of establishing this trust is to help for the poor people, we are sure to attain and achieve peace, happiness and satisfaction. As old saying goes “Serving Human beings is equal to serving God”."
        chariman = findViewById(R.id.chariman)
        txt_chair_msg = findViewById(R.id.txt_chair_msg)
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        txt_chair_msg.text = Html.fromHtml(sourceString)
        Glide.with(this)
                .load(R.drawable.chairmen)
                .apply(RequestOptions.circleCropTransform())
                .into(chariman)
        recyclerView = findViewById(R.id.list_horizontal)
        recyclerView.layoutManager = LinearLayoutManager(this@AboutusActivity)
        recyclerView.setHasFixedSize(true)
        adapter = CoverFlowAdapter()
        adapter.CoverFlowAdapter(this@AboutusActivity, aboutUsModel)
        recyclerView.adapter = adapter
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
}
