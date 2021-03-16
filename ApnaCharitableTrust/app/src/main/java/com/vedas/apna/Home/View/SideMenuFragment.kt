package com.vedas.apna.Home.View

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.Home.Adapter.MenuListAdapter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import de.hdodenhof.circleimageview.CircleImageView

class SideMenuFragment : Fragment() {
    private lateinit var img_pic:CircleImageView
    private lateinit var txt_name: TextView
    private lateinit var categoriesRecyclerview: RecyclerView
    private lateinit var rl_pic: RelativeLayout
    private lateinit var mtoggle: ActionBarDrawerToggle
    private lateinit var mdrawerLayout: DrawerLayout
    //lateinit var fragmentDrawerListener: FragmentDrawerListener
    private var vieww: View? = null
    lateinit var menulistadpater: MenuListAdapter
    private var array = ArrayList<String>()
    private lateinit var userEditText: EditText
    private lateinit var bt_count:Button

    lateinit var pref : SharedPreferences
    lateinit var mobile:String
    lateinit var profilepic:String
    lateinit var name:String
    lateinit var accesstoken:String
    lateinit var email:String

    var notifylocalhandler = Handler()
    var apiDelayed = /*1.2 * */900 //1 second=1000 milisecond, 5*1000=5seconds
    var chatInterrupt = false
    private var withoutchatInterrupt = false

    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            //do your function;
            if (!chatInterrupt) {
                menulistadpater.fetchNotification(activity!!, mobile, false, "Home")
            }
            notifylocalhandler.postDelayed(this, apiDelayed.toLong())
        }
    }
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vieww=view
        pref = activity!!.getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        init(view)
    }

    /////Intialize the view
    fun init(view: View) {
        txt_name = view.findViewById(R.id.txt_name)
        img_pic = view.findViewById(R.id.img_pic)
        rl_pic = view.findViewById(R.id.rl_pic)
        categoriesRecyclerview = view.findViewById(R.id.recycle)
        arraydataadding()
        img_pic.setOnClickListener(img_picclick)
        txt_name.setOnClickListener(txt_nameclick)
    }
    private val img_picclick = View.OnClickListener {
        if (mobile != "null"){
            chatInterrupt = true
            mdrawerLayout.closeDrawers()
            startActivity(Intent(activity, UpdateProfileActivity::class.java).putExtra("photoURI", profilepic))
        }
    }
    @SuppressLint("InflateParams")
    private val txt_nameclick = View.OnClickListener {
        if (mobile != "null"){
            //mdrawerLayout.closeDrawers()
            chatInterrupt = true
            val layoutInflaterAndroid = LayoutInflater.from(activity)
            val mView = layoutInflaterAndroid.inflate(R.layout.user_status_dialog_box, null)
            val alertDialogBuilderUserInput = AlertDialog.Builder(activity!!)
            alertDialogBuilderUserInput.setView(mView)

            userEditText = mView.findViewById(R.id.userInputDialog)
            userEditText.setText(txt_name.text.toString())
           alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Update") { _, _ -> // ToDo get user input here
                        menulistadpater.NameUpdate(activity!!,userEditText.text.toString(),mobile,profilepic,img_pic,mdrawerLayout,txt_name)
                        //userStatusNotificationFunction(userEditText.getText().toString(), users.getUserVisible(), users.isNotifications(), "status")
                    }
                    .setNegativeButton("Cancel"
                    ) { dialogBox, id -> dialogBox.cancel() }

            val alertDialogAndroid = alertDialogBuilderUserInput.create()
            alertDialogAndroid.setCanceledOnTouchOutside(true)
            alertDialogAndroid.show()
            alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity!!,R.color.black))
            alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity!!,R.color.orange))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).typeface = this.resources.getFont(R.font.arialmedium)
                alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).typeface = resources.getFont(R.font.arialmedium)
            } else {
                alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).typeface =
                    ResourcesCompat.getFont(activity!!, R.font.arialmedium)
                alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).typeface =
                    ResourcesCompat.getFont(activity!!, R.font.arialmedium)
            }
            //startActivity(Intent(getActivity(),UpdateProfileActivity::class.java).putExtra("photoURI", profilepic))
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun arraydataadding() {
        array.add("Home")
        array.add("About Us")
        array.add("Gallery")
        array.add("Contact Us")
        array.add("Documents")
        array.add("QR Code")
        array.add("Rate Us")
        array.add("Share")
        if (mobile != "null"){
            array.add("Change Password")
            array.add("Sign Out")
            rl_pic.visibility = View.VISIBLE
            Glide.with(activity!!)
                    .load(ServerApiCollection.BASE_URL1 + profilepic.substring(1))
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_pic)
            txt_name.text = name
            Handler().postDelayed({
                menulistadpater.fetchNotification(activity!!, mobile, false, "Home")
            }, 800)

        }else{
            Glide.with(activity!!)
                    .load(ContextCompat.getDrawable(activity!!,R.drawable.logo_new))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .skipMemoryCache(true)
                    .into(img_pic)
            txt_name.text = "APNA\nCHARITABLE\nTRUST"
            rl_pic.visibility = View.VISIBLE
            //rl_pic.setVisibility(View.GONE)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (mobile != "null"){
            UniversalImageLoader.setImage(
                    profilepic.substring(1),
                    img_pic,
                    null,
                    ServerApiCollection.BASE_URL1
            )
            chatInterrupt = false
            notifylocalhandler.postDelayed(mRunnable, apiDelayed.toLong())
            //txt_name.setText(getActivity()!!.getSharedPreferences("LoginPref", 0).getString("name",null).toString())
        }else {
            Glide.with(activity!!)
                    .load(ContextCompat.getDrawable(activity!!,R.drawable.logo_new))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .skipMemoryCache(true)
                    .into(img_pic)
            txt_name.text = "APNA\nCHARITABLE\nTRUST"
        }
    }

    override fun onPause() {
        super.onPause()
        notifylocalhandler.removeCallbacks(mRunnable) //stop handler when activity not visibles
    }

    override fun onDestroy() {
        super.onDestroy()
        notifylocalhandler.removeCallbacks(mRunnable) //stop handler when activity not visible
        chatInterrupt = false
        withoutchatInterrupt = false
    }

/*fun setFragmentDrawerListner(drawerListner: FragmentDrawerListener) {
        this.fragmentDrawerListener = drawerListner
    }*/

    fun setup(fragment1: Int, drawer: DrawerLayout, toolbar: Toolbar, bt_count: Button) {
        this.bt_count = bt_count
        vieww = activity?.findViewById(fragment1)
        mdrawerLayout = drawer
        val menuListAdapter = activity?.let { MenuListAdapter(it, array, mdrawerLayout, mobile,
                accesstoken,chatInterrupt,bt_count) }
        if (menuListAdapter != null) {
            this.menulistadpater = menuListAdapter
        }
        categoriesRecyclerview.layoutManager = LinearLayoutManager(activity)
        categoriesRecyclerview.adapter = menuListAdapter
        categoriesRecyclerview.isNestedScrollingEnabled = false
        RetrofitCallbacks.getInstace().initializeServerInterface(menuListAdapter)
        mtoggle = object : ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                activity?.invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity?.invalidateOptionsMenu()
            }
        }
        mdrawerLayout.addDrawerListener(mtoggle)
        mtoggle.isDrawerIndicatorEnabled = false
        mdrawerLayout.post { }
    }

    /*fun setDrawerListener(listener: FragmentDrawerListener) {
        this.fragmentDrawerListener = listener
    }*/
}
