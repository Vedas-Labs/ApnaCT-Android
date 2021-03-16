package com.vedas.apna.Forgot.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.vedas.apna.Forgot.Presenter.ForgotPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks

class ForgotActivity : AppCompatActivity(),IForgotView{
    private lateinit var username:EditText
    private lateinit var getotp : Button
    private lateinit var rl_back: RelativeLayout
    private lateinit var img_back: ImageView
    private lateinit var forgotPresenter : ForgotPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        init()
    }
    private fun init() {
        username = findViewById(R.id.username)
        getotp = findViewById(R.id.getotp)
        rl_back = findViewById(R.id.rl_back)
        img_back = findViewById(R.id.img_back)
        forgotPresenter = ForgotPresenter()
        forgotPresenter.ForgotPresenter(this)
        RetrofitCallbacks.getInstace().initializeServerInterface(forgotPresenter)
        rl_back.setOnClickListener(backclick)
        img_back.setOnClickListener(backclick)
        getotp.setOnClickListener { OTPResult() }
    }
    private val backclick = View.OnClickListener {
        onBackPressed()
    }
    private fun OTPResult() {
        //RetrofitCallbacks.getInstace().initializeServerInterface(forgotPresenter)
        forgotPresenter.doForgot(this@ForgotActivity,username)
    }

    override fun otpSuccess(success: String) {
        startActivity(Intent(this@ForgotActivity, VerificationActivity::class.java)
                .putExtra("email",success))
    }

    override fun otpfail() {
        TODO("Not yet implemented")
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}