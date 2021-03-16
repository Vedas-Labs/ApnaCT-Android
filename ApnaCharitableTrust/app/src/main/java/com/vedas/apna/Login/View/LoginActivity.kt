package com.vedas.apna.Login.View

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vedas.apna.Forgot.View.ForgotActivity
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.Login.Presenter.LoginPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks


class LoginActivity : AppCompatActivity(), LoginView  {
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var txt_new : TextView
    private lateinit var cancel : TextView
    private lateinit var txt_forgot : TextView
    private lateinit var loginbtn : Button
    private lateinit var img_pswd_eye : ImageView
    private lateinit var rl_eye : RelativeLayout
    private lateinit var presenter : LoginPresenter
    private var eye1:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }
    private fun init() {
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginbtn = findViewById(R.id.login)
        txt_new = findViewById(R.id.txt_new)
        cancel = findViewById(R.id.cancel)
        txt_forgot = findViewById(R.id.txt_forgot)
        rl_eye = findViewById(R.id.rl_eye)
        img_pswd_eye = findViewById(R.id.img_pswd_eye)
        rl_eye.setOnClickListener(rl_eyeclick)
        presenter = LoginPresenter()
        presenter.LoginPresenter(this,this@LoginActivity)
        txt_new.setText(presenter.setClickablePart("New Member? REGISTER",this,username,password), TextView.BufferType.SPANNABLE)
        // don't forget this ! or this will not work !
        txt_new.movementMethod = LinkMovementMethod.getInstance()
        txt_new.highlightColor = Color.TRANSPARENT
        loginbtn.setOnClickListener { loginResult() }
        cancel.setOnClickListener { onBackPressed() }
        txt_forgot.setOnClickListener { v ->
            val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            if (imm.isAcceptingText) {
                Log.e("shown", "")
                imm.hideSoftInputFromWindow(
                        v?.windowToken,
                        InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
            } else {
                Log.e("unshown", "")
            }
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_eyeclick = View.OnClickListener {
        if (!eye1){
            password.post { password.setSelection(password.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity,R.drawable.eye_on))
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye1=true
        }else{
            password.post { password.setSelection(password.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity,R.drawable.eye_off))
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            eye1=false
        }
    }
    private fun loginResult() {
        //val usertxt: String = username.text.toString()
        //val pswdtxt: String = password.text.toString()
        presenter = LoginPresenter()
        presenter.LoginPresenter(this,this@LoginActivity)
        RetrofitCallbacks.getInstace().initializeServerInterface(presenter)
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isAcceptingText) {
            Log.e("shown", "")
            imm.hideSoftInputFromWindow(
                loginbtn.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        } else {
            Log.e("unshown", "")
        }
        presenter.doLogin(this, username, password)
    }

    override fun loginresult(Success: String) {
        Toast.makeText(this, Success, Toast.LENGTH_SHORT).show()
        finishAffinity()
        startActivity(
            Intent(this@LoginActivity, HomeActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        )
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }
}
/*window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }*/