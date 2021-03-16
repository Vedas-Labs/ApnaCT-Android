package com.vedas.apna.ServerConnections

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class AppStatus {
    private var connectivityManager: ConnectivityManager? = null
    private var connected = false
    fun isConnected(): Boolean {
        try {
            connectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager!!.activeNetworkInfo
            connected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            return connected
        } catch (e: Exception) {
            println("CheckConnectivity Exception: " + e.message)
            Log.v("connectivity", e.toString())
        }
        return connected
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val instance = AppStatus()
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        fun getInstance(ctx: Context): AppStatus {
            context = ctx.applicationContext
            return instance
        }
    }
}