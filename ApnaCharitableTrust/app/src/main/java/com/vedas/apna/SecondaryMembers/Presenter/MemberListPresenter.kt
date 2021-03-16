package com.vedas.apna.SecondaryMembers.Presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.SecondaryMembers.Adapter.SecondaryMembersListAdapter
import com.vedas.apna.SecondaryMembers.Model.SecondaryList
import com.vedas.apna.SecondaryMembers.Model.SecondaryMemberListResponse
import com.vedas.apna.SecondaryMembers.View.SecondaryMembersListView
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject

class MemberListPresenter:RetrofitCallbacks.ServerResponseInterface {
    lateinit var context: Context
    lateinit var mobile: String
    private var secondaryList:ArrayList<SecondaryList>?=null
    private lateinit var recyclerView: RecyclerView
    private var membersListView: SecondaryMembersListView? = null

    fun MemberListPresenter(context: Context,mobile: String,recyclerView: RecyclerView,membersListView: SecondaryMembersListView ) {
        this.context = context
        this.mobile=mobile
        this.recyclerView=recyclerView
        this.membersListView=membersListView
    }

    fun fetchSecondaryList() {
        if (AppStatus.getInstance(context).isConnected()) {
            var fetchSecondaryListObj = JsonObject()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("mobileNumber", mobile)
                val jsonParser = JsonParser()
                fetchSecondaryListObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                //print parameter
                Log.e("FetchSecondaryListReq:", " $fetchSecondaryListObj")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitCallbacks.getInstace().ApiCallbacks(context, "payments/secondMemberList", fetchSecondaryListObj, "fetchSecondaryList")
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successCallBack(body: String?, from: String?) {
        Log.e("ress", ": $body")
        val gson = Gson()
        val res: SecondaryMemberListResponse = gson.fromJson(body, SecondaryMemberListResponse::class.java)
        membersListView?.responseView(res.message!!)
        secondaryList=res.secondaryList
        val adapter = SecondaryMembersListAdapter()
        adapter.SecondaryMembersListAdapter(context,secondaryList!!)
        recyclerView.adapter=adapter
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        membersListView?.responseView(failureMsg!!)
    }
}