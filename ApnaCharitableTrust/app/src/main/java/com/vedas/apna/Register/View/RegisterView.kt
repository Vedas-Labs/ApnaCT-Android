package com.vedas.apna.Register.View

interface RegisterView {
    fun registerSuccess(success : String)
    fun registerfail()
    fun fetchrefnumber(refnumber : String)
    fun ToastMsg(toastmsg: String)
}