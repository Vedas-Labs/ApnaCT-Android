package com.vedas.apna.Notifications.Presenter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Notifications.Adapter.NotificationAdapter
import com.vedas.apna.Notifications.Model.NotificationModel
import com.vedas.apna.Notifications.Model.NotifyInbox
import com.vedas.apna.Notifications.View.INotificationView
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NotificationPresenter : INotificationpresenter, RetrofitCallbacks.ServerResponseInterface {
    lateinit var context: Context
    private lateinit var iNotificationView: INotificationView
    private var notifyInboxx:ArrayList<NotifyInbox> = ArrayList()
    private var readArray: ArrayList<String> = ArrayList()
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var recycle_notify: RecyclerView
    var from: String = ""
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    fun NotificationPresenter(context: Context, iNotificationView: INotificationView,
                              notificationAdapter: NotificationAdapter, recycle_notify: RecyclerView,
                              swipeRefreshLayout: SwipeRefreshLayout) {
        this.context = context
        this.iNotificationView = iNotificationView
        this.notificationAdapter = notificationAdapter
        this.recycle_notify = recycle_notify
        this.swipeRefreshLayout = swipeRefreshLayout
    }

    override fun fetchNotification(context: Context, mobileNumber: String, withoutchatInterrupt: Boolean, from: String) {
        this.from = from
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileNumber)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(
                context, "notification/fetchNotification", loginObj, from)
    }

    override fun readNotification(context: Context, mobileNumber: String, notificationID: String, from: String) {
        this.from = from
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileNumber)
            jsonObject.put("notificationID", notificationID)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(
                context, "notification/readNotification", loginObj, from)
    }

    override fun deleteNotification(context: Context, mobileNumber: String, notificationID: String, from: String) {
        this.from = from
        this.context = context
        ProgressDialog.getInstance().showProgress(context)
        val msgid = ArrayList<String>()
        msgid.add(notificationID)
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileNumber)
            jsonObject.put("notificationIDs", JSONArray(msgid))
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacksDelete(context, loginObj, from, notificationID)
    }

    override fun successCallBack(body: String?, from: String?) {
        Log.e("ress", ": $body")
        val jsonObject: JSONObject?
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (from.equals("Home")) {
                        val gson = Gson()
                        val res: NotificationModel = gson.fromJson(body, NotificationModel::class.java)
                        notifyInboxx = ArrayList()
                        if (res.memberInbox.isEmpty()){
                            Log.e("ress menu", ":ressssssssssssss")
                        }else {
                            Log.e("ress menuuu", ":resss"+ res.memberInbox[0].notifyInbox.size.toString())
                            res.memberInbox[0].let {
                                notifyInboxx = it.notifyInbox as ArrayList<NotifyInbox>
                            }
                        }
                        var bool = false
                        var pos = 0
                        readArray = ArrayList()
                        for (inbox in 0 until notifyInboxx.size) {
                            if (!notifyInboxx[inbox].isRead) {
                                bool = true
                            }
                            if (bool) {
                                pos++
                                readArray.add((pos).toString())
                                bool = false
                            }
                        }
                        Log.e("ress", " homee" + readArray.size.toString())
                        notificationAdapter.NotificationAdapter(context, notifyInboxx, NotificationPresenter())
                        recycle_notify.adapter = notificationAdapter
                        notificationAdapter.notifyDataSetChanged()
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        iNotificationView.onNotificationSuccess(readArray.size.toString())
                    } else if (from.equals("read")) {
                        Log.e("ress", " read" + jsonObject.getString("message"))
                    } else {
                        val gson = Gson()
                        val res: NotificationModel = gson.fromJson(body, NotificationModel::class.java)
                        notifyInboxx = ArrayList()
                        if (res.memberInbox.isEmpty()){
                            Log.e("ress menu", ":ressssssssssssss")
                        }else {
                            Log.e("ress menuuu", ":resss"+ res.memberInbox[0].notifyInbox.size.toString())
                            res.memberInbox[0].let {
                                notifyInboxx = it.notifyInbox as ArrayList<NotifyInbox>
                            }
                        }
                        var bool = false
                        var pos = 0
                        readArray = ArrayList()
                        for (inbox in 0 until notifyInboxx.size) {
                            if (!notifyInboxx[inbox].isRead) {
                                bool = true
                            }
                            if (bool) {
                                pos++
                                readArray.add((pos).toString())
                                bool = false
                            }
                        }
                        notificationAdapter.NotificationAdapter(context, notifyInboxx, NotificationPresenter())
                        recycle_notify.adapter = notificationAdapter
                        notificationAdapter.notifyDataSetChanged()
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        //iNotificationView.onNotificationSuccess(jsonObject.getString("response"))
                    }
                } else {
                    iNotificationView.Toastmsg(jsonObject.getString("message"))
                    ProgressDialog.getInstance().hideProgress()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {
        Log.e("ress", ": $body")
        var jsonObject: JSONObject?
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (from.equals("delete")) {
                        iNotificationView.failure(notificationID.toString())
                        ProgressDialog.getInstance().hideProgress()
                        Log.e("ress", " delete:: " + notificationID.toString())
                    }
                } else {
                    iNotificationView.Toastmsg(jsonObject.getString("message"))
                    ProgressDialog.getInstance().hideProgress()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun failureCallBack(failureMsg: String?) {
        if (from == "read") {
            Log.e("ress", " read" + failureMsg.toString())
        } else {
            iNotificationView.Toastmsg(failureMsg.toString())
            ProgressDialog.getInstance().hideProgress()
        }
    }
}