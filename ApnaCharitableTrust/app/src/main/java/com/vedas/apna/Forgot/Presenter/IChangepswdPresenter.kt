package com.vedas.apna.Forgot.Presenter

import android.content.Context
import android.widget.EditText

interface IChangepswdPresenter {
    fun doChangePswd(context: Context, new_pswd: EditText,reset_pswd : EditText,email:String,from: String)
}