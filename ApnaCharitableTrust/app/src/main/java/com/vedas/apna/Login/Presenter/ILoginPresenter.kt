package com.vedas.apna.Login.Presenter

import android.content.Context
import android.text.SpannableStringBuilder
import android.widget.EditText

interface ILoginPresenter {
    fun doLogin(context: Context, username: EditText,password: EditText)
    fun setClickablePart(str: String,context: Context, username: EditText,password: EditText):SpannableStringBuilder
}