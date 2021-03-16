package com.vedas.apna.Home.View

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.vedas.apna.R

class QRCodeActivity :AppCompatActivity(){
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
}
