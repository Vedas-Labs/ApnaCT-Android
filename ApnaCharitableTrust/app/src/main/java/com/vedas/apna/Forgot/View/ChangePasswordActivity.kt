@file:Suppress("PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName", "PrivatePropertyName")

package com.vedas.apna.Forgot.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vedas.apna.Forgot.Presenter.ChangePswdPresenter
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.Login.View.LoginActivity
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIOssNS")
class ChangePasswordActivity : AppCompatActivity(),IChangepswdView {
    private lateinit var rl_back: RelativeLayout
    private lateinit var img_back: ImageView
    private lateinit var ed_pswd: EditText
    private lateinit var new_pswd: EditText
    private lateinit var txt_rest: TextView
    private lateinit var done: Button
    private lateinit var changePswdPresenter : ChangePswdPresenter
    private lateinit var email: String
    private lateinit var rl_eye: RelativeLayout
    private lateinit var rl_set_eye: RelativeLayout
    private lateinit var img_pswd_eye: ImageView
    private lateinit var img_setpswd_eye: ImageView
    private var eye1:Boolean = false
    private var eye2:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepswd)
        email = intent.getStringExtra("email").toString()
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        rl_back = findViewById(R.id.rl_back)
        img_back = findViewById(R.id.img_back)
        ed_pswd = findViewById(R.id.ed_pswd)
        new_pswd = findViewById(R.id.new_pswd)
        txt_rest = findViewById(R.id.txt_rest)
        rl_set_eye = findViewById(R.id.rl_set_eye)
        rl_eye = findViewById(R.id.rl_eye)
        img_setpswd_eye = findViewById(R.id.img_setpswd_eye)
        img_pswd_eye = findViewById(R.id.img_pswd_eye)
        done = findViewById(R.id.done)
        if (intent.getStringExtra("from").toString() == "menu"){
            txt_rest.text = "Change Password"
            ed_pswd.hint = "Old Password"
            new_pswd.hint = "New Password"
        }
        changePswdPresenter = ChangePswdPresenter()
        changePswdPresenter.ChangePswdPresenter(this)
        RetrofitCallbacks.getInstace().initializeServerInterface(changePswdPresenter)
        rl_back.setOnClickListener(backclick)
        img_back.setOnClickListener(backclick)
        done.setOnClickListener(doneclick)
        rl_eye.setOnClickListener(rl_eyeclick)
        rl_set_eye.setOnClickListener(rl_set_eyeclick)
    }
    private val backclick = View.OnClickListener {
        onBackPressed()
    }
    private val doneclick = View.OnClickListener {
        changePswdPresenter.doChangePswd(this,ed_pswd,new_pswd,email, intent.getStringExtra("from").toString())
    }
    /*fun SuccessAlertDialog() {
        val dialog = Dialog(this@ChangePasswordActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.success_popup)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btn_unfriend = dialog.findViewById<RelativeLayout>(R.id.rl_cancel)
        val txt_username = dialog.findViewById<TextView>(R.id.txt_username)
        txt_username.text = "Your password has been reset Successfully!!"
        //txt_username.setOnClickListener { dialog.dismiss() }
        btn_unfriend.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
            startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
        }
        dialog.show()
    }*/

    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_eyeclick = View.OnClickListener {
        if (!eye1){
            ed_pswd.post { ed_pswd.setSelection(ed_pswd.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@ChangePasswordActivity,R.drawable.eye_on))
            ed_pswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye1=true
        }else{
            ed_pswd.post { ed_pswd.setSelection(ed_pswd.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@ChangePasswordActivity,R.drawable.eye_off))
            ed_pswd.transformationMethod = PasswordTransformationMethod.getInstance()
            eye1=false
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_set_eyeclick = View.OnClickListener {
        if (!eye2){
            new_pswd.post { new_pswd.setSelection(new_pswd.length()) }
            img_setpswd_eye.setImageDrawable(ContextCompat.getDrawable(this@ChangePasswordActivity,R.drawable.eye_on))
            new_pswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye2=true
        }else{
            new_pswd.post { new_pswd.setSelection(new_pswd.length()) }
            img_setpswd_eye.setImageDrawable(ContextCompat.getDrawable(this@ChangePasswordActivity,R.drawable.eye_off))
            new_pswd.transformationMethod = PasswordTransformationMethod.getInstance()
            eye2=false
        }
    }

    override fun pswdSuccess(success: String) {
        //SuccessAlertDialog()
        ToastMsg("Your password has been reset Successfully!!")
        finishAffinity()
        startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
    }

    override fun pswdfail() {
        finishAffinity()
        startActivity(Intent(this@ChangePasswordActivity, HomeActivity::class.java))
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this,toastmsg, Toast.LENGTH_SHORT).show()
    }
}
