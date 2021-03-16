package com.vedas.apna.Forgot.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.poovam.pinedittextfield.SquarePinField
import com.vedas.apna.Forgot.Presenter.VerificationPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks

class VerificationActivity : AppCompatActivity(),IForgotView {
    private lateinit var txt_new:TextView
    private lateinit var rl_back:RelativeLayout
    private lateinit var img_back:ImageView
    private lateinit var getotp:Button
    private lateinit var squareField:SquarePinField
    private lateinit var email: String
    private lateinit var verifyPresenter : VerificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        init()
    }

    private fun init() {
        email = intent.getStringExtra("email").toString()
        txt_new = findViewById(R.id.txt_new)
        rl_back = findViewById(R.id.rl_back)
        img_back = findViewById(R.id.img_back)
        getotp = findViewById(R.id.getotp)
        squareField = findViewById(R.id.squareField)
        val text = Html.fromHtml("Not yet received? Resend").toString()
        txt_new.movementMethod = LinkMovementMethod.getInstance()
        txt_new.setText(setClickablePart(text), TextView.BufferType.SPANNABLE)
        txt_new.highlightColor = Color.TRANSPARENT

        verifyPresenter = VerificationPresenter()
        verifyPresenter.VerificationPresenter(this)
        RetrofitCallbacks.getInstace().initializeServerInterface(verifyPresenter)
        rl_back.setOnClickListener(backclick)
        img_back.setOnClickListener(backclick)
        getotp.setOnClickListener(getotpclick)
    }
    private val backclick = View.OnClickListener {
        onBackPressed()
    }
    private val getotpclick = View.OnClickListener {
        verifyPresenter.doVerify(this@VerificationActivity,email,squareField,"verifyotp")
        //verifyPresenter.doVerify(this@VerificationActivity,email)
    }
    private fun setClickablePart(str: String): SpannableStringBuilder {
        val m_spannableStringBuilder = SpannableStringBuilder(str)
        val m_index = str.indexOf("Resend")
        val clickString = str.substring(m_index, str.length)
        m_spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                verifyPresenter.doVerify(this@VerificationActivity,email,squareField,"resend")
                //startActivity(Intent(this@VerificationActivity, RegisterActivity::class.java))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.blue)
                ds.bgColor = Color.TRANSPARENT
                ds.isUnderlineText = false
            }
        }, m_index, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return m_spannableStringBuilder
    }

    override fun otpSuccess(success: String) {
        startActivity(Intent(this@VerificationActivity, ChangePasswordActivity::class.java)
            .putExtra("email",email).putExtra("from","verify"))
    }

    override fun otpfail() {
        TODO("Not yet implemented")
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this,toastmsg, Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        init()
    }
}
