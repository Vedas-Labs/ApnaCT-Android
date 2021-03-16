package com.vedas.apna.Donate.View

interface IDonateView {
    fun ToastMsg(toastmsg: String)
    fun donateSuccess(success : String)
    fun donatefail()
    fun offlineSuccess()
    fun fetchrefnumber(refnumber : String)
    fun paytmSuccess()
}