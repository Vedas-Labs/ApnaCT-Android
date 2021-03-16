package com.vedas.apna.Home.View

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.vedas.apna.BankDetails.View.UploadBankDetails
import com.vedas.apna.Donate.View.DonateActivity
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.Home.Adapter.GoalsListAdapter
import com.vedas.apna.Home.Adapter.ProjectListAdapter
import com.vedas.apna.Home.MainProjectsModel
import com.vedas.apna.Login.View.LoginActivity
import com.vedas.apna.Notifications.View.NotificationActivity
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.SessionManager
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private lateinit var education: Array<String>
    private lateinit var medicalCamp: Array<String>
    private lateinit var ruralDevelopmentProgramme: Array<String>
    private lateinit var sportsProgramme: Array<String>
    private lateinit var culturalProgramme: Array<String>
    private lateinit var emergencyReliefe: Array<String>
    private var mainProjectsModels: MutableList<MainProjectsModel> = ArrayList()
    private lateinit var mainProjectRecyclerView: RecyclerView
    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var projectImage: ImageView
    private lateinit var carouselView: CarouselView
    private lateinit var drawer: DrawerLayout
    private lateinit var sideMenuFragment: SideMenuFragment
    //lateinit var drawerFragment: SideMenuFragment
    lateinit var toolbar: Toolbar
    lateinit var rl_back:RelativeLayout
    private lateinit var rl_notify:RelativeLayout
    private lateinit var rl_member:RelativeLayout
    private lateinit var rl_donate:RelativeLayout
    private lateinit var li_mem:LinearLayout
    private lateinit var li_don:LinearLayout
    lateinit var img_back:ImageView
    private lateinit var img_notify:ImageView
    private lateinit var framelayout:FrameLayout
    private lateinit var bt_count:Button
    lateinit var pref :SharedPreferences

    private var doubleBackToExitPressedOnce = false

    private var sampleImages = intArrayOf(
        R.drawable.education,
        R.drawable.medicalcamp2,
        R.drawable.rural,
        R.drawable.sports,
        R.drawable.cultural_programs,
        R.drawable.emergency
    )
    private lateinit var listGoals: Array<String>
    lateinit var mobile:String
    lateinit var profilepic:String
    lateinit var name:String
    lateinit var accesstoken:String
    lateinit var email:String

    private val REQUEST_CODE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.e(
            "home", "1" + getSharedPreferences("apna", MODE_PRIVATE).getString(
                SessionManager.KEY_USERID,
                ""
            )
        )
        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        Log.e("home", "1.1$mobile")
        //inAppUpdate()
        init()
        modelfunadding()
        //checkingloginstatus()
        val projectListAdapter = ProjectListAdapter(
            this,
            mainProjectsModels,
            mainProjectRecyclerView
        )
        mainProjectRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        )
        mainProjectRecyclerView.adapter = projectListAdapter
        mainProjectRecyclerView.isNestedScrollingEnabled = false
        carouselView = findViewById<View>(R.id.carouselView) as CarouselView
        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)
        //sideMenuFragment.setFragmentDrawerListner(this)
        rl_back.setOnClickListener(menuclick)
        img_back.setOnClickListener(menuclick)
        rl_notify.setOnClickListener(notifyclick)
        img_notify.setOnClickListener(notifyclick)
        framelayout.setOnClickListener(notifyclick)
        bt_count.setOnClickListener(notifyclick)
        li_mem.setOnClickListener(memberclick)
        li_don.setOnClickListener(donateclick)

    }

    private fun checkingloginstatus() {
        Log.e("home", "1.2$mobile")
        if (mobile != "null"){
            rl_notify.visibility = View.VISIBLE
        }else{
            rl_notify.visibility = View.GONE
        }
    }

    private val menuclick = View.OnClickListener {
        drawer.openDrawer(GravityCompat.START)
    }

    private val notifyclick = View.OnClickListener {
        startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
    }

    private val memberclick = View.OnClickListener {
        if (mobile != "null"){
            startActivity(Intent(this@HomeActivity, UploadBankDetails::class.java))
        }else{
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        }
    }

    private val donateclick = View.OnClickListener {
        //Toast.makeText(this, "In Progress", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@HomeActivity, DonateActivity::class.java))
    }

    //initializing the navigation drawer
    /*fun initializeDrawer() {
        drawer = findViewById(R.id.drawerlayoutabout)
        drawerFragment = (supportFragmentManager.findFragmentById(R.id.fragment1) as SideMenuFragment)
        drawerFragment.setup( R.id.fragment1, drawer, toolbar)
        //drawerFragment.setDrawerListener(this)
    }*/
    private fun modelfunadding() {
        mainProjectsModels.add(
            MainProjectsModel(
                "Education",
                R.drawable.education.toString(),
                education
            )
        )
        mainProjectsModels.add(
            MainProjectsModel(
                "Medical Camps",
                R.drawable.medicalcamp2.toString(),
                medicalCamp
            )
        )
        mainProjectsModels.add(
            MainProjectsModel(
                "Rural Development Programs",
                R.drawable.rural.toString(),
                ruralDevelopmentProgramme
            )
        )
        mainProjectsModels.add(
            MainProjectsModel(
                "Sports Programs",
                R.drawable.sports.toString(),
                sportsProgramme
            )
        )
        mainProjectsModels.add(
            MainProjectsModel(
                "Cultural Programs",
                R.drawable.cultural_programs.toString(),
                culturalProgramme
            )
        )
        mainProjectsModels.add(
            MainProjectsModel(
                "Emergency Relief",
                R.drawable.emergency.toString(),
                emergencyReliefe
            )
        )
    }

    private fun init() {
        initImageLoader()
        mainProjectRecyclerView = findViewById(R.id.mainProjectRecyclerView)
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        projectImage = findViewById(R.id.projectImage)
        education = resources.getStringArray(R.array.Education)
        medicalCamp = resources.getStringArray(R.array.Medical_Camps)
        ruralDevelopmentProgramme = resources.getStringArray(R.array.Rural_Development_Programs)
        sportsProgramme = resources.getStringArray(R.array.Sports_Programs)
        culturalProgramme = resources.getStringArray(R.array.Cultural_Programs)
        emergencyReliefe = resources.getStringArray(R.array.Emergency_Relief)
        drawer = findViewById(R.id.drawerlayoutabout)
        rl_donate = findViewById(R.id.rl_donate)
        rl_member = findViewById(R.id.rl_member)
        rl_back = findViewById(R.id.rl_back)
        img_back = findViewById(R.id.img_back)
        rl_notify = findViewById(R.id.rl_notify)
        img_notify = findViewById(R.id.img_notify)
        framelayout = findViewById(R.id.framelayout)
        bt_count = findViewById(R.id.bt_count)
        li_mem = findViewById(R.id.li_mem)
        li_don = findViewById(R.id.li_don)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        sideMenuFragment = supportFragmentManager.findFragmentById(R.id.fragment1) as SideMenuFragment
        sideMenuFragment.setup(R.id.fragment1, drawer, toolbar, bt_count)
        //initializeDrawer()
    }
    private fun initImageLoader() {
        val universalImageLoader = UniversalImageLoader(this@HomeActivity)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }

    private var imageListener =
        ImageListener { position, imageView -> imageView.setImageResource(sampleImages[position]) }

    fun displayGoals(title: String?, pic: String) {

        when (title) {
            "Education" -> {
                listGoals = education
                projectImage.setImageResource(R.drawable.education)
            }
            "Medical Camps" -> {
                listGoals = medicalCamp
                projectImage.setImageResource(R.drawable.medicalcamp2)
            }
            "Rural Development Programs" -> {
                listGoals = ruralDevelopmentProgramme
                projectImage.setImageResource(R.drawable.rural)
            }
            "Sports Programs" -> {
                listGoals = sportsProgramme
                projectImage.setImageResource(R.drawable.sports)
            }
            "Cultural Programs" -> {
                listGoals = culturalProgramme
                projectImage.setImageResource(R.drawable.cultural_programs)
            }
            "Emergency Relief" -> {
                listGoals = emergencyReliefe
                projectImage.setImageResource(R.drawable.emergency)
            }
            else -> {
            }
        }
        // goalsRecyclerView=findViewById(R.id.goalsRecyclerView);
        Log.e("asjhdhs", pic)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)
        val goalsListAdapter = GoalsListAdapter(this@HomeActivity, listGoals)
        goalsRecyclerView.adapter = goalsListAdapter
        goalsRecyclerView.isNestedScrollingEnabled = false
    }

    override fun onResume() {
        super.onResume()
        Log.e("home", "2")
        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        checkingloginstatus()
        init()
        //inResumeAppUpdate()
    }

   /* private fun inAppUpdate() {
        Log.e("asjkhfjd1", "sjnfjsdn")
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this@HomeActivity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        Log.e("asjkhfjd2", "sjnfjsdn$appUpdateInfoTask")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.e("asjkhfjd3", "sjnfjsdn")
            Log.e("jdnjfndjk1", "nk: " + appUpdateInfo.updateAvailability())
            Log.e("jdnjfndjk2", "nk: " + UpdateAvailability.UPDATE_AVAILABLE)
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.e("asjkhfjd4", "sjnfjsdn")
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@HomeActivity,
                        REQUEST_CODE
                    )
                } catch (e: SendIntentException) {
                    Log.e("asjkhfjd5", "sjnfjsdn")
                    e.printStackTrace()
                }
            }
        }
    }
    private fun inResumeAppUpdate() {
        Log.e("resume1", "sjnfjsdn")
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this@HomeActivity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        Log.e("resume2", "sjnfjsdn$appUpdateInfoTask")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.e("resume3", "sjnfjsdn")
            Log.e("resume4", "nk: " + appUpdateInfo.updateAvailability())
            Log.e("resume5", "nk: " + UpdateAvailability.UPDATE_AVAILABLE)
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                Log.e("resume6", "sjnfjsdn")
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@HomeActivity,
                        REQUEST_CODE
                    )
                } catch (e: SendIntentException) {
                    Log.e("resume7", "sjnfjsdn")
                    e.printStackTrace()
                }
            }else {
                Log.d("TAG", "No Update available")
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(this@HomeActivity, "start Downloading", Toast.LENGTH_SHORT).show()
            if (resultCode != RESULT_OK) {
                Log.e("dsjfbjh", "Update flow failed$resultCode")
            }
        }
    }*/

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            if (doubleBackToExitPressedOnce) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}