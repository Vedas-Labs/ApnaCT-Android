package com.vedas.apna.Home.View

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vedas.apna.R


class ContactusActivity :AppCompatActivity(){
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var txt_address: TextView
    private lateinit var txt_web: TextView
    private lateinit var txt_mail: TextView
    private lateinit var txt_phone: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        txt_address = findViewById(R.id.txt_address)
        txt_phone = findViewById(R.id.txt_phone)
        txt_web = findViewById(R.id.txt_web)
        txt_mail = findViewById(R.id.txt_mail)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        txt_address.setOnClickListener(addressclick)
        /*txt_phone.setPaintFlags(View.INVISIBLE);
        txt_phone.setMovementMethod(LinkMovementMethod.getInstance());
        txt_phone.setHighlightColor(Color.TRANSPARENT);*/
        phonespannablefun()
        mailspannablefun()
        webspannablefun()

    }

    private fun webspannablefun() {
        txt_web.movementMethod = LinkMovementMethod.getInstance()
        txt_web.highlightColor = Color.TRANSPARENT
        val sa : Spannable = txt_web.text as Spannable
        for (u in sa.getSpans(0, sa.length, URLSpan::class.java)) {
            sa.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, sa.getSpanStart(u), sa.getSpanEnd(u), 0)
        }
    }

    private fun mailspannablefun() {
        txt_mail.movementMethod = LinkMovementMethod.getInstance()
        txt_mail.highlightColor = Color.TRANSPARENT
        val sa : Spannable = txt_mail.text as Spannable
        for (u in sa.getSpans(0, sa.length, URLSpan::class.java)) {
            sa.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, sa.getSpanStart(u), sa.getSpanEnd(u), 0)
        }
    }

    private fun phonespannablefun() {
        txt_phone.movementMethod = LinkMovementMethod.getInstance()
        txt_phone.highlightColor = Color.TRANSPARENT
        val sa : Spannable = txt_phone.text as Spannable
        for (u in sa.getSpans(0, sa.length, URLSpan::class.java)) {
            sa.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, sa.getSpanStart(u), sa.getSpanEnd(u), 0)
        }
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
    private val addressclick = View.OnClickListener {
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/Rajahmsa+Siva+sai,+624,+Main+Rd,+opp.+SBI+ATM,+Ramachandra+Nagar,+Anantapur,+Andhra+Pradesh+515001/@14.6851059,77.5972115,15z/data=!3m1!4b1"))
        startActivity(browser)
    }
}
