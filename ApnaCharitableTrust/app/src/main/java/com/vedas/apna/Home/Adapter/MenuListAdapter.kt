@file:Suppress("unused")

package com.vedas.apna.Home.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.nostra13.universalimageloader.core.ImageLoader
import com.vedas.apna.Documents.View.DocumentsActivity
import com.vedas.apna.Forgot.View.ChangePasswordActivity
import com.vedas.apna.Gallery.View.GalleryActivity
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.Home.View.AboutusActivity
import com.vedas.apna.Home.View.ContactusActivity
import com.vedas.apna.Home.View.QRCodeActivity
import com.vedas.apna.Notifications.Model.NotificationModel
import com.vedas.apna.Notifications.Model.NotifyInbox
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import com.vedas.apna.ServerConnections.SessionManager
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class MenuListAdapter(
        context: FragmentActivity,
        array: ArrayList<String>,
        mdrawerLayout: DrawerLayout,
        mobile: String,
        accesstoken: String,
        chatInterrupt: Boolean,
        bt_count: Button
) : RecyclerView.Adapter<MenuListAdapter.ViewHolder>(), RetrofitCallbacks.ServerResponseInterface {
    private var contexts: FragmentActivity = context
    var arraylist = ArrayList<String>()
    private lateinit var mdrawerLayout: DrawerLayout
    var mobile: String
    var accesstoken: String
    lateinit var profilepic: String
    var name: String? = null
    private lateinit var img_pic: CircleImageView
    private lateinit var txt_name: TextView
    private var chatInterrupt: Boolean = false
    private var notifyInboxx:ArrayList<NotifyInbox> = ArrayList()
    private var readArray:ArrayList<String> = ArrayList()
    private var bt_count:Button
    init {
        this.arraylist = array
        this.mdrawerLayout = mdrawerLayout
        this.mobile = mobile
        this.accesstoken = accesstoken
        this.chatInterrupt = chatInterrupt
        this.bt_count = bt_count
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        private var rl_list: RelativeLayout = itemView.findViewById(R.id.rl_list)

    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_menu, null)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = arraylist[position]
        holder.itemView.setOnClickListener {
            when (holder.adapterPosition) {
                0 -> {
                    mdrawerLayout.closeDrawers()
                    //contexts.startActivity(Intent(contexts, HomeActivity::class.java))
                }
                1 -> {
                    chatInterrupt = true
                    contexts.startActivity(Intent(contexts, AboutusActivity::class.java))
                }
                2 -> {
                    chatInterrupt = true
                    contexts.startActivity(Intent(contexts, GalleryActivity::class.java))
                }
                3 -> {
                    chatInterrupt = true
                    contexts.startActivity(Intent(contexts, ContactusActivity::class.java))
                }
                4 -> {
                    chatInterrupt = true
                    contexts.startActivity(Intent(contexts, DocumentsActivity::class.java))
                }
                5 -> {
                    chatInterrupt = true
                    contexts.startActivity(Intent(contexts, QRCodeActivity::class.java))
                }
                6 -> {
                    showAlertDialog(contexts)
                }
                7 -> {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Apna Charitable Trust")
                        val content =
                                """Join me on Apna Charitable Trust india for social work and lot of earn on each referral.
                                
                                If you don't have Apna Charitable Trust(ACT) yet, you can download it here for free and try it out.
                                
                                
                                """.trimIndent()
                        var shareMessage = "Android : "
                        shareMessage = """
                                $content${shareMessage}https://play.google.com/store/apps/details?id=com.vedas.apna&hl=en_IN
                                
                                Web : https://apnacharitabletrust.org/#/home
                                """.trimIndent()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        contexts.startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }
                8 -> {
                    chatInterrupt = true
                    contexts.startActivity(
                            Intent(contexts, ChangePasswordActivity::class.java)
                                    .putExtra(
                                            "email", contexts.getSharedPreferences("LoginPref", 0).getString(
                                            "userEmail",
                                            null
                                    )
                                    )
                                    .putExtra("from", "menu")
                    )
                }
                9 -> {
                    LogoutAlertDialog(contexts)
                }
            }
            mdrawerLayout.closeDrawers()
        }
    }

    private fun showAlertDialog(context: FragmentActivity) {
        Log.e("logggg", "out")
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.rating_alert)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btnRateUs = dialog.findViewById<Button>(R.id.btn_rating)
        btnRateUs.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.vedas.apna&hl=en_IN")
            )
            context.startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("HardwareIds")
    private fun LogoutAlertDialog(context: FragmentActivity) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.logout_popup)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btn_unfriend = dialog.findViewById<Button>(R.id.btn_unfriend)
        val txt_cancel = dialog.findViewById<TextView>(R.id.txt_cancel)
        txt_cancel.setOnClickListener { dialog.dismiss() }
        btn_unfriend.setOnClickListener {
            chatInterrupt = true
            dialog.dismiss()
            if (AppStatus.getInstance(context).isConnected()) {
                ProgressDialog.getInstance().showProgress(context)
                val token = FirebaseInstanceId.getInstance().token
                val ID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                var profileObj = JsonObject()
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("mobileNumber", mobile)
                    jsonObject.put("deviceID", ID)
                    jsonObject.put("deviceToken", token)
                    jsonObject.put("deviceType", "mobile")
                    val jsonParser = JsonParser()
                    profileObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                    //print parameter
                    Log.e("LOGINJSON:", " $profileObj")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                RetrofitCallbacks.getInstace().ApiCallbacksLogout(context, accesstoken, "apna/logout", profileObj, "logout")
            } else {
                Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    fun NameUpdate(context: Context, userEditText: String, mobile: String, profilepic: String, img_pic: CircleImageView,
                   mdrawerLayout: DrawerLayout,
                   txt_name: TextView) {
        ProgressDialog.getInstance().showProgress(context)
        this.profilepic = profilepic
        this.img_pic = img_pic
        this.name = userEditText
        this.mdrawerLayout = mdrawerLayout
        this.txt_name = txt_name
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobile)
            jsonObject.put("fullName", userEditText)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/updateName", loginObj, "updatename")
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun successCallBack(body: String?, from: String?) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (from.equals("logout")) {
                        if (SessionManager(contexts).isLoggedIn) {
                            SessionManager(contexts).logoutUser()
                        }
                    }else if (from.equals("Home")) {
                        Log.e("ress menu", ": $body")
                        val gson = Gson()
                        val res: NotificationModel = gson.fromJson(body, NotificationModel::class.java)
                        notifyInboxx = ArrayList()
                        if (res.memberInbox.isEmpty()){
                            Log.e("ress menu", ":ressssssssssssss")
                        }else {
                            Log.e("ress menuuu", ":resss"+ res.memberInbox[0].notifyInbox.size.toString())
                            res.memberInbox[0].let {
                                notifyInboxx = it.notifyInbox as ArrayList<NotifyInbox>
                            }
                        }
                        var bool = false
                        var pos = 0
                        readArray = ArrayList()
                        for (inbox in 0 until notifyInboxx.size) {
                            if (!notifyInboxx[inbox].isRead) {
                                bool = true
                            }
                            if (bool) {
                                pos++
                                readArray.add((pos).toString())
                                bool = false
                            }
                        }
                        Log.e("ress", " menu"+readArray.size.toString())
                        if (readArray.size>0){
                            bt_count.visibility = View.VISIBLE
                            bt_count.text = readArray.size.toString()
                        }else{
                            bt_count.visibility = View.GONE
                        }
                    }else{
                        if (name != null){
                            Log.e("menu :", name)
                            val pref = contexts.getSharedPreferences("LoginPref", 0) // 0 - for private mode
                            val editor = pref.edit()
                            editor.putString("name", name)
                            editor.apply()
                            try {
                                trimCache(contexts)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                            ImageLoader.getInstance().clearDiskCache()
                            ImageLoader.getInstance().clearMemoryCache()
                            UniversalImageLoader.setImage(
                                    profilepic.substring(1),
                                    img_pic,
                                    null,
                                    ServerApiCollection.BASE_URL1
                            )
                            txt_name.text = name
                        }
                        ProgressDialog.getInstance().hideProgress()
                    }
                } else {
                    ProgressDialog.getInstance().hideProgress()
                }
            } else {
                ProgressDialog.getInstance().hideProgress()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    private fun trimCache(context: Context) {
        Log.e("cacheee", "trim")
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else {
            false
        }
    }
    override fun failureCallBack(failureMsg: String?) {
        Toast.makeText(contexts, failureMsg, Toast.LENGTH_SHORT).show()
        ProgressDialog.getInstance().hideProgress()
    }
    fun fetchNotification(context: Context, mobileNumber: String, withoutchatInterrupt: Boolean, from: String) {
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileNumber)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(context, "notification/fetchNotification", loginObj, from)
    }
}
