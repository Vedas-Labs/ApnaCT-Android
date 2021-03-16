package com.vedas.apna.SecondaryMembers.View

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.R
import com.vedas.apna.SecondaryMembers.Presenter.MemberListPresenter
import com.vedas.apna.ServerConnections.RetrofitCallbacks

class SecondaryMembersActivity : AppCompatActivity(),SecondaryMembersListView {
    private lateinit var secondaryMembersRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var presenter : MemberListPresenter
    private lateinit var back: RelativeLayout
    lateinit var img_back: ImageView
    lateinit var pref : SharedPreferences
    var mobile:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary_members)
        init()
    }

    private fun init() {
        pref = getSharedPreferences("LoginPref", 0)
        mobile = pref.getString("mobileNumber", null).toString()
        secondaryMembersRecyclerView = findViewById(R.id.secondaryMembersListRecycler)
        back=findViewById(R.id.rl_back)
        img_back=findViewById(R.id.img_back)
        linearLayoutManager = LinearLayoutManager(this)
        secondaryMembersRecyclerView.layoutManager = linearLayoutManager
        presenter = MemberListPresenter()
        presenter.MemberListPresenter(this@SecondaryMembersActivity,mobile,secondaryMembersRecyclerView,this)
        RetrofitCallbacks.getInstace().initializeServerInterface(presenter)
        presenter.fetchSecondaryList()
        img_back.setOnClickListener(backClick)
        back.setOnClickListener(backClick)
    }
    private val backClick = View.OnClickListener {
        onBackPressed()
    }
    override fun responseView(res: String) {
        Log.e("asdbsads", "asdsa$res")
        Toast.makeText(this@SecondaryMembersActivity,res,Toast.LENGTH_SHORT).show()
    }
}