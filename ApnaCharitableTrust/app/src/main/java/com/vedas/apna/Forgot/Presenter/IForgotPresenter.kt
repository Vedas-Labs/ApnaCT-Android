package com.vedas.apna.Forgot.Presenter

import android.content.Context
import android.widget.EditText
import com.poovam.pinedittextfield.SquarePinField

interface IForgotPresenter {
    fun doForgot(context: Context, username: EditText)
    fun doVerify(context: Context, username: String,squareField:SquarePinField,from:String)
}