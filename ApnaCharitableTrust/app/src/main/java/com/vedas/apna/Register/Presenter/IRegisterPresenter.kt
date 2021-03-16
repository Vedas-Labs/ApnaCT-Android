package com.vedas.apna.Register.Presenter

import android.content.Context
import android.widget.EditText

interface IRegisterPresenter {
    fun dofetchrefnumber(context: Context,refmobileno: String)
    fun doRegister(context: Context, refmobileno: EditText, username:EditText, email: EditText,
                   mobileno:EditText, password: EditText, retypepswd:EditText,image:String)
}