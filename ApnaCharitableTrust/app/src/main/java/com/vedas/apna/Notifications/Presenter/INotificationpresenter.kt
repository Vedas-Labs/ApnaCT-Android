package com.vedas.apna.Notifications.Presenter

import android.content.Context

interface INotificationpresenter {
    fun fetchNotification(context: Context, mobileNumber: String, withoutchatInterrupt: Boolean, from: String)
    fun readNotification(context: Context, mobileNumber: String, notificationID: String, from: String)
    fun deleteNotification(context: Context, mobileNumber: String, notificationID: String, from: String)
}