package com.vedas.apna.Notifications.View

interface INotificationView {
    fun onNotificationSuccess(Success : String)
    fun failure(msg:String)
    fun Toastmsg(toastmsg:String)
}