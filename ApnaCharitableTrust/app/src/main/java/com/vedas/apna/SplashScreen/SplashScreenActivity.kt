package com.vedas.apna.SplashScreen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.Notifications.View.NotificationView
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.SessionManager
import com.vedas.apna.ServerConnections.SessionManager.KEY_USERID


@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var text_title: TextView
    private val SPLASH_TIME_OUT = 2000 //3000
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        statusBarTransparent()
        text_title = findViewById(R.id.txt_title)
        //beautifulParagraph(text_title)

        Handler().postDelayed({ redirect() }, SPLASH_TIME_OUT.toLong())
    }

    private fun redirect() {
        val sessionManager = SessionManager(this@SplashScreenActivity)
        Log.e("checkLogin", " " + sessionManager.isLoggedIn)
        if (sessionManager.isLoggedIn) {
            if (intent.hasExtra("notifyID")) {
                sharedPreferences = getSharedPreferences("apna", MODE_PRIVATE)
                val userID: String? = sharedPreferences.getString(KEY_USERID, "")
                Log.e("userId", " $userID")
                Log.e("djbffdg", "djnsdjf2222222" + intent.getStringExtra("notifyImages"))
                val selectedEntry: String = intent.getStringExtra("notifyImages").toString()
                val intentt = Intent(this, NotificationView::class.java)
                val args = Bundle()
                if (selectedEntry == "[]") {
                    args.putStringArrayList("ARRAYLIST", ArrayList())
                } else {
                    val parts: List<String>
                    val jsonArray: String = selectedEntry
                    Log.e("lists123", " :" + jsonArray.subSequence(1, jsonArray.length - 1))

                    val ss: String = jsonArray.subSequence(1, jsonArray.length - 1).toString().replace("\\", "").replace("\"", "")
                    parts = ss.split(",")
                    Log.e("listreplace", " :" + jsonArray + " listsize:" + parts[0])
                    args.putStringArrayList("ARRAYLIST", ArrayList<String>(parts))
                }
                intentt.putExtra("BUNDLE", args)
                intentt.putExtra("title", intent.getStringExtra("title"))
                intentt.putExtra("body", intent.getStringExtra("body"))
                intentt.putExtra("time", System.currentTimeMillis().toString())
                Log.e("listreplace", " :" + " listsize:")
                intentt.putExtra("id", intent.getStringExtra("notifyID"))
                if (isAppOnForeground(this, packageName)) {
                    intentt.putExtra("from", "foreground")
                } else {
                    intentt.putExtra("from", "b")
                }
                startActivity(intentt)
            } else {
                sharedPreferences = getSharedPreferences("apna", MODE_PRIVATE)
                val userID: String? = sharedPreferences.getString(KEY_USERID, "")
                Log.e("userId", " $userID")
                startActivity(
                        Intent(applicationContext, HomeActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
            }
        } else {
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        finish()
    }
    private fun isAppOnForeground(context: Context, appPackageName: String): Boolean {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == appPackageName) {
                //                Log.e("app",appPackageName);
                return true
            }
        }
        return false
    }

    private fun statusBarTransparent() {
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

}
