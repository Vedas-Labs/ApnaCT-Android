package com.vedas.apna.BankDetails.Presenter

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.EditText

interface IBankPresenter {
    fun doupload(context: Context, name: EditText, acNumber: EditText, acNumber2: EditText, bankName: AutoCompleteTextView, branchName: EditText, ifsCode: EditText,
                 villageMandel: EditText, dist: EditText, state: EditText, /*email: EditText,*/ type:String, mobile:String)
    fun fetchBankDetail(context: Context,mobile: String)
}