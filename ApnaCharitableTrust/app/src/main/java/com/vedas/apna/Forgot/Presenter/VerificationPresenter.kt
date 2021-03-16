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

class VerificationPresenter :IForgotPresenter,RetrofitCallbacks.ServerResponseInterface{
    private var ForgotResult: IForgotView? = null
    private var squareField: SquarePinField? = null
    fun VerificationPresenter(iforgotResult: IForgotView) {
        this.ForgotResult = iforgotResult
    }
    override fun doForgot(context: Context, username: EditText) {

    }

    override fun doVerify(
        context: Context,
        username: String,
        squareField: SquarePinField,
        from: String
    ) {
        this.squareField = squareField
        if (from == "verifyotp"){
            if (squareField.length() == 4){
                if (AppStatus.getInstance(context).isConnected()) {
                    ProgressDialog.getInstance().showProgress(context)
                    Handler().postDelayed({
                        /* ForgotResult?.otpSuccess("Change your password")
                         ForgotResult?.ToastMsg("Your OTP is 1234")*/
                        var loginObj = JsonObject()
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("mobileNumber", username)
                            jsonObject.put("otp", squareField.text.toString())
                            val jsonParser = JsonParser()
                            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                            //print parameter
                            Log.e("LOGINJSON:", " $loginObj")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/verifyMember", loginObj, "verify")
                    }, 1000)
                } else {
                    ForgotResult?.ToastMsg("No Internet Connection!!!!")
                }
            }else{
                ForgotResult?.ToastMsg("enter otp")
            }
        }else{
            if (username.isNotEmpty()){
                if (AppStatus.getInstance(context).isConnected()) {
                    ProgressDialog.getInstance().showProgress(context)
                    Handler().postDelayed({
                        /* ForgotResult?.otpSuccess("Change your password")
                         ForgotResult?.ToastMsg("Your OTP is 1234")*/
                        var loginObj = JsonObject()
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("mobileNumber", username)
                            val jsonParser = JsonParser()
                            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                            //print parameter
                            Log.e("LOGINJSON:", " $loginObj")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/forgetpassword", loginObj, "OTP")
                    }, 1000)
                } else {
                    ForgotResult?.ToastMsg("No Internet Connection!!!!")
                }
            }else{
                ForgotResult?.ToastMsg("Email.ID is empty!!")
            }
        }

    }

    override fun successCallBack(body: String?,from:String) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (jsonObject.getString("response").equals("3")) {
                if (from == "OTP"){
                    if (squareField?.equals(null) != true){
                        squareField?.setText("")
                        squareField?.requestFocus()
                        ProgressDialog.getInstance().hideProgress()
                    }
                }else{
                    ForgotResult?.otpSuccess(jsonObject.getString("message"))
                    ProgressDialog.getInstance().hideProgress()
                }
            } else {
                ForgotResult?.ToastMsg(jsonObject.getString("message"))
                ProgressDialog.getInstance().hideProgress()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        Log.e("verify", "failure$failureMsg")
        ProgressDialog.getInstance().hideProgress()
    }
}