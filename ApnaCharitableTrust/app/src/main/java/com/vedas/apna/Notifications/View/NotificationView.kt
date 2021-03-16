package com.vedas.apna.Notifications.View

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vedas.apna.Gallery.Adapter.GalleryAdapter
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.Notifications.Adapter.NotificationAdapter
import com.vedas.apna.Notifications.Presenter.NotificationPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class NotificationView:AppCompatActivity(),INotificationView {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var li_notify_txt: LinearLayout
    private lateinit var txt_date: Button
    private lateinit var txt_message: TextView
    private lateinit var txt_titlee: TextView
    private lateinit var recycle_notify: RecyclerView
    lateinit var adapter: GalleryAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var imagesArray: ArrayList<String>
    private lateinit var notifyid:String
    private lateinit var notificationPresenter: NotificationPresenter
    private var nm: NotificationManager? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        init()
    }
    private fun init() {
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        recycle_notify = findViewById(R.id.recycle_notify)
        li_notify_txt = findViewById(R.id.li_notify_txt)
        txt_date = findViewById(R.id.txt_date)
        txt_message = findViewById(R.id.txt_message)
        txt_titlee = findViewById(R.id.txt_titlee)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        li_notify_txt.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = false
        imagesArray = ArrayList()

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        imagesArray = args.getStringArrayList("ARRAYLIST") as ArrayList<String>
        val cal = Calendar.getInstance(TimeZone.getTimeZone("ASIA/KOLKATA"))
        cal.timeInMillis = intent.getStringExtra("time")!!.toLong()
        val date: String = DateFormat.format("dd-MM-yyyy", cal).toString()
        val time: String = DateFormat.format("hh:mm:ss", cal).toString()
        txt_date.text = date
        txt_titlee.text = intent.getStringExtra("title")
        txt_message.text = intent.getStringExtra("body")
        notifyid = intent.getStringExtra("id")
        gridLayoutManager = GridLayoutManager(this@NotificationView, 2)
        recycle_notify.layoutManager = gridLayoutManager
        recycle_notify.setHasFixedSize(true)
        adapter = GalleryAdapter()
        adapter.GalleryAdapter(this@NotificationView, ArrayList(), ArrayList(), imagesArray, "notifications")
        recycle_notify.adapter = adapter
        notificationPresenter = NotificationPresenter()
        notificationPresenter.NotificationPresenter(this@NotificationView, this, NotificationAdapter(), recycle_notify, SwipeRefreshLayout(this))
        RetrofitCallbacks.getInstace().initializeServerInterface(notificationPresenter)
        notificationPresenter.readNotification(this@NotificationView,
                getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(), notifyid, "read")
        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        Log.e("tag notify", " manager" + Arrays.toString(nm!!.activeNotifications))
        for (notification in nm!!.activeNotifications) {
            cancelNotification(notification.id, notification.tag)
        }
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getStringExtra("from").equals("foreground")){
            finish()
        }else{
            finishAffinity()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
    private fun cancelNotification(id: Int, tag: String) {
        //you can get notificationManager like this:
        //notificationManage r= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (tag == intent.getStringExtra("id")) {
            nm!!.cancel(tag, id)
        }
    }
    override fun onNotificationSuccess(Success: String) {

    }

    override fun failure(msg: String) {

    }

    override fun Toastmsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }
}