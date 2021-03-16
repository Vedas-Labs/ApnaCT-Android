package com.vedas.apna.Forgot.Presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.EditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.poovam.pinedittextfield.SquarePinField
import com.vedas.apna.Forgot.View.IForgotView
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject

class ForgotPresenter :IForgotPresenter,RetrofitCallbacks.ServerResponseInterface{
    private var ForgotResult: IForgotView? = null
    private lateinit var memberId: String
    fun ForgotPresenter(iforgotResult: IForgotView) {
        this.ForgotResult = iforgotResult
    }
    override fun doForgot(context: Context, username: EditText) {
        if (username.text.isNotEmpty()){
            if (username.text.length == 10){
            if (AppStatus.getInstance(context).isConnected()) {
                ProgressDialog.getInstance().showProgress(context)
                Handler().postDelayed({
                    /*ForgotResult?.otpSuccess(username.getText().toString())
                    ForgotResult?.ToastMsg("Your OTP is 1234")*/
                    var loginObj = JsonObject()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("mobileNumber", username.text.toString())
                        val jsonParser = JsonParser()
                        loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                        //print parameter
                        Log.e("LOGINJSON:", " $loginObj")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    memberId = username.text.toString()
                    RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/forgetpassword", loginObj, "OTP")
                }, 1000)
            } else {
                ForgotResult?.ToastMsg("No Internet Connection!!!!")
            }
            }else{
                ForgotResult?.ToastMsg("Enter 10 digits only")
            }
        }else{
            ForgotResult?.ToastMsg("Enter Membership ID")
        }
    }

    override fun doVerify(
        context: Context,
        username: String,
        squareField: SquarePinField,
        from: String
    ) {
        TODO("Not yet implemented")
    }

    override fun successCallBack(body: String?,from:String) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            Log.e("forgot", "success$jsonObject")
            if (jsonObject.getString("response").equals("3")) {
                ForgotResult?.otpSuccess(memberId)
                //ForgotResult?.ToastMsg("Your OTP is "+jsonObject.getString("Otp"))
            } else {
                ForgotResult?.ToastMsg(jsonObject.getString("message"))
            }
            ProgressDialog.getInstance().hideProgress()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        Log.e("forgot", "failure$failureMsg")
        ProgressDialog.getInstance().hideProgress()
    }
}