@file:Suppress("LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName")

package com.vedas.apna.Login.Presenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Login.View.LoginView
import com.vedas.apna.R
import com.vedas.apna.Register.View.RegisterActivity
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.SessionManager
import org.json.JSONException
import org.json.JSONObject

class LoginPresenter : ILoginPresenter, RetrofitCallbacks.ServerResponseInterface {
    private var LoginResult: LoginView? = null
    private var handler: Handler? = null
    lateinit var context: Context

    fun LoginPresenter(iLoginResult: LoginView, context: Context) {
        this.LoginResult = iLoginResult
        this.context = context
        handler = Handler(Looper.getMainLooper())
    }

    override fun doLogin(context: Context, username: EditText, password: EditText) {
        if (username.text.isNotEmpty()) {
            if (username.text.length == 10) {
                if (password.text.isNotEmpty()) {
                    if (AppStatus.getInstance(context).isConnected()) {
                        ProgressDialog.getInstance().showProgress(context)
                    loginApi(context, username.text.toString(), password.text.toString())
                    //LoginResult?.loginresult("Login Successfull")
                    } else {
                        Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    LoginResult?.ToastMsg("Password is empty")
                }
            } else {
                LoginResult?.ToastMsg("Membership ID 10 digits only")
            }
            } else {
                LoginResult?.ToastMsg("Membership ID is empty")
            }
        }

    override fun setClickablePart(
            str: String,
            context: Context,
            username: EditText,
            password: EditText
    ): SpannableStringBuilder {
        val m_spannableStringBuilder = SpannableStringBuilder(str)
        val m_index = str.indexOf("REGISTER")
        val clickString = str.substring(m_index, str.length)
        m_spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                username.setText("")
                password.setText("")
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(context,R.color.blue)
                ds.bgColor = Color.TRANSPARENT
                ds.isUnderlineText = false
            }
        }, m_index, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return m_spannableStringBuilder
    }

    override fun successCallBack(body: String?, from: String) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (jsonObject.getString("response").equals("3")){
                val sessionManager = SessionManager(context)
                sessionManager.createLoginSession(jsonObject.getJSONObject("userInfo").getString("mobileNumber"))
                val pref = context.getSharedPreferences("LoginPref", 0) // 0 - for private mode
                val editor = pref.edit()
                editor.putString("accesstoken", jsonObject.getString("access_token"))
                editor.putString("mobileNumber", jsonObject.getJSONObject("userInfo").getString("mobileNumber"))
                editor.putString("userEmail", jsonObject.getJSONObject("userInfo").getString("emailID"))
                editor.putString("profilepic", jsonObject.getJSONObject("userInfo").getString("image"))
                editor.putString("name", jsonObject.getJSONObject("userInfo").getString("fullName"))
                editor.putBoolean("isBankUpdated", jsonObject.getJSONObject("userInfo").getBoolean("isBankUpdated"))
                editor.apply()
                LoginResult?.loginresult(jsonObject.getString("message"))
            }else{
                LoginResult?.ToastMsg(jsonObject.getString("message"))
            }
            ProgressDialog.getInstance().hideProgress()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        Log.e("Loginactivity", "$failureMsg")
        LoginResult?.ToastMsg(failureMsg.toString())
        ProgressDialog.getInstance().hideProgress()
    }

    private fun loginApi(context: Context, username: String, password: String) {
        val token = FirebaseInstanceId.getInstance().token
        val ID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", username)
            jsonObject.put("password", password)
            jsonObject.put("deviceID", ID)
            jsonObject.put("deviceToken", token)
            jsonObject.put("deviceType", "mobile")
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/login", loginObj, "login")
    }
}

