package com.vedas.apna.Donate.Presenter

import android.content.Context
import android.widget.EditText
import android.widget.Spinner
import java.io.File

interface IDonatePresenter {
    fun dofetchrefnumber(context: Context,refmobileno: String)
    fun dosubmit(context: Context,mobile: EditText,dName: EditText, dMobile:EditText, pan: EditText,emailid: EditText,address: EditText,
                 dAmount: EditText, tran_date: EditText,spin_deposit: Spinner,spin: Spinner,files: ArrayList<File>,type:String)
}