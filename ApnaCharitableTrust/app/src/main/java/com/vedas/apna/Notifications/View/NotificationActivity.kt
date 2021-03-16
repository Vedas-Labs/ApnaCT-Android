package com.vedas.apna.Notifications.View

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.vedas.apna.Helper.SwiperHelper
import com.vedas.apna.Notifications.Adapter.NotificationAdapter
import com.vedas.apna.Notifications.Model.NotifyInbox
import com.vedas.apna.Notifications.Presenter.NotificationPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import java.util.*


class NotificationActivity : AppCompatActivity(), INotificationView, OnRefreshListener {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var recycle_notify: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var notifyArray: ArrayList<NotifyInbox> = ArrayList()
    lateinit var notificationAdapter: NotificationAdapter
    //var memberInbox= MemberInbox.instance
    private lateinit var notificationPresenter: NotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        init()
    }

    private fun init() {
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        recycle_notify = findViewById(R.id.recycle_notify)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this@NotificationActivity)
        val c1 = ContextCompat.getColor(this@NotificationActivity,R.color.orange)
        val c2 = ContextCompat.getColor(this@NotificationActivity,R.color.blue)
        val c3 = ContextCompat.getColor(this@NotificationActivity,R.color.green)
        val c4 = ContextCompat.getColor(this@NotificationActivity,R.color.red)

        swipeRefreshLayout.setColorSchemeColors(c1, c2, c3, c4)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycle_notify.layoutManager = LinearLayoutManager(this@NotificationActivity)
        recycle_notify.setHasFixedSize(true)
        notificationAdapter = NotificationAdapter()
        if (notifyArray.size > 0) {
            notificationAdapter.NotificationAdapter(this@NotificationActivity, notifyArray, notificationPresenter)
            recycle_notify.adapter = notificationAdapter
        } else {
            notificationPresenter = NotificationPresenter()
            notificationPresenter.NotificationPresenter(this@NotificationActivity, this, notificationAdapter, recycle_notify, swipeRefreshLayout)
            RetrofitCallbacks.getInstace().initializeServerInterface(notificationPresenter)
            notificationPresenter.fetchNotification(this@NotificationActivity,
                    getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(), false, "Notification")
            /*notificationAdapter.NotificationAdapter(this@NotificationActivity, notifyArray,notificationPresenter)
            recycle_notify.setAdapter(notificationAdapter)*/
        }
        val swiperHelper: SwiperHelper = object : SwiperHelper(this@NotificationActivity, recycle_notify, 200) {
            override fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder?, buffer: MutableList<MyButton>) {
                buffer.add(MyButton(this@NotificationActivity, R.drawable.delete_sweep, Color.parseColor("#FBF8F8"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                notificationAdapter.callDeleteFunction(pos)
                            }
                        }))
            }
        }
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

    override fun onNotificationSuccess(Success: String) {

    }

    override fun failure(msg:String) {
        Log.e("poss", " $msg")
        this.notificationAdapter.callDeleteposition(msg)
    }

    override fun Toastmsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        notificationPresenter.NotificationPresenter(this@NotificationActivity, this, notificationAdapter, recycle_notify, swipeRefreshLayout)
        RetrofitCallbacks.getInstace().initializeServerInterface(notificationPresenter)
        notificationPresenter.fetchNotification(this@NotificationActivity,
                getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(), false, "Notification")
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}