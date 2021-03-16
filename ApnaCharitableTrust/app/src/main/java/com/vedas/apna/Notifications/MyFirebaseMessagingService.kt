package com.vedas.apna.Notifications
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.Notifications.View.NotificationView
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.SessionManager
import java.util.*
import kotlin.collections.ArrayList


@Suppress("CAST_NEVER_SUCCEEDS")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var pendingIntent: PendingIntent? = null
    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("MyFirebaseMessagingService")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.e("ajsfkj", "dhkfjhf" + Gson().toJson(remoteMessage))
        Log.e("ajsfkj", "dhkfjhf" + remoteMessage.notification!!.title)
        Log.e("ajsfkjsjdj", "dhkfjhf" + remoteMessage.notification!!.body)
        Log.e("ajsfkjsjdj", "dhkfjhf" + remoteMessage.notification!!.notificationCount)
        showNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        Log.e("tokenfirebase", token)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "com.vedas.apna.test"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "hello"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = R.color.orange
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val parts: List<String>
        Log.e("djbffdg", "djnsdjf2222222" + remoteMessage.data)
        if (remoteMessage.data["notifyImages"].toString() == "[]"){
            parts = ArrayList()
        }else{
            var jsonArray = ""
            jsonArray = remoteMessage.data["notifyImages"].toString()
            Log.e("lists123", " :" + jsonArray.subSequence(1, jsonArray.length - 1))

            val ss:String = jsonArray.subSequence(1, jsonArray.length - 1).toString().replace("\\", "").replace("\"", "")
            parts = ss.split(",")
            Log.e("listreplace", " :" + jsonArray + " listsize:" + parts[0])
        }

        val intent = Intent(this, NotificationView::class.java)
        val args = Bundle()
        if (remoteMessage.data["notifyImages"].toString() == "[]"){
            args.putStringArrayList("ARRAYLIST", ArrayList())
        }else{
            args.putStringArrayList("ARRAYLIST", ArrayList<String>(parts))
        }
        intent.putExtra("BUNDLE", args)
        intent.putExtra("title", remoteMessage.notification?.title)
        intent.putExtra("body", remoteMessage.notification?.body)
        intent.putExtra("time", remoteMessage.sentTime.toString())
        Log.e("listreplace", " :"  + " listsize:")
        intent.putExtra("id", remoteMessage.data["notifyID"].toString())
        if (isAppOnForeground(this, packageName)) {
            intent.putExtra("from", "foreground")
        }else{
            intent.putExtra("from", "b")
        }
        pendingIntent = PendingIntent.getActivity(this,
                System.currentTimeMillis().toInt(), intent,
                PendingIntent.FLAG_ONE_SHOT)
        Log.e("firebasee", "djnjf$intent")
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        val sessionManager = SessionManager(this@MyFirebaseMessagingService)
        var badgenumber = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (sessionManager.isLoggedIn) {
                Log.e("djbd", "djnjf")
                notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.logo_2)
                        .setContentTitle(remoteMessage.notification?.title)
                        .setContentText(remoteMessage.notification?.body)
                        .setContentInfo("Info").setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            } else {
                Log.e("djdffb", "djnsdjf")
                pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, HomeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
                notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.logo_2)
                        .setContentTitle(remoteMessage.notification?.title)
                        .setContentText(remoteMessage.notification?.body)
                        .setContentInfo("Info").setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            }
        } else {
            if (sessionManager.isLoggedIn) {
                Log.e("djbdd", "djnsdjf")
                notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.logo_2)
                        .setContentTitle(remoteMessage.notification?.title)
                        .setContentText(remoteMessage.notification?.body)
                        .setContentInfo("Info").setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            } else {
                Log.e("djbffdg", "djnsdjf")
                pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, HomeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.logo_2)
                            .setContentTitle(remoteMessage.notification?.title)
                            .setContentText(remoteMessage.notification?.body)
                            .setContentInfo("Info").setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                }
            }
        }

        notificationManager.notify(remoteMessage.data["notifyID"].toString(),Random().nextInt(), notificationBuilder.build())
    }
    private fun isAppOnForeground(context: Context, appPackageName: String): Boolean {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == appPackageName) {
                //                Log.e("app",appPackageName);
                return true
            }
        }
        return false
    }
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
