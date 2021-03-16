@file:Suppress("FunctionName")

package com.vedas.apna.Forgot.Presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.EditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.Forgot.View.IChangepswdView
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChangePswdPresenter : IChangepswdPresenter, RetrofitCallbacks.ServerResponseInterface {
    private var ChangepswdResult: IChangepswdView? = null
    private var fromm : String? = null

    fun ChangePswdPresenter(ichangepswd: IChangepswdView) {
        this.ChangepswdResult = ichangepswd
    }
    override fun doChangePswd(context: Context, new_pswd: EditText, reset_pswd: EditText, email: String, from: String) {
        this.fromm = from
        if (new_pswd.text.isNotEmpty()) {
            if (reset_pswd.text.isNotEmpty()) {
                if (from == "menu"){
                    if (reset_pswd.text.toString() != new_pswd.text.toString()) {
                        if (AppStatus.getInstance(context).isConnected()) {
                            ProgressDialog.getInstance().showProgress(context)
                            Handler().postDelayed({
                                //ChangepswdResult?.pswdSuccess(new_pswd.getText().toString())
                                var loginObj = JsonObject()
                                val jsonObject = JSONObject()
                                try {
                                    jsonObject.put("mobileNumber", email)
                                    jsonObject.put("oldPassword", new_pswd.text.toString())
                                    jsonObject.put("newPassword", reset_pswd.text.toString())
                                    val jsonParser = JsonParser()
                                    loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                                    //print parameter
                                    Log.e("LOGINJSON:", " $loginObj")
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/changepassword", loginObj, "OTP")
                            }, 1000)
                        } else {
                            ChangepswdResult?.ToastMsg("No Internet Connection!!!!")
                        }
                    } else {
                        reset_pswd.requestFocus()
                        ChangepswdResult?.ToastMsg("Please check new password & old password is same !!")
                    }
                }else {
                    if (reset_pswd.text.toString() == new_pswd.text.toString()) {
                        if (AppStatus.getInstance(context).isConnected()) {
                            ProgressDialog.getInstance().showProgress(context)
                            Handler().postDelayed({
                                //ChangepswdResult?.pswdSuccess(new_pswd.getText().toString())
                                var loginObj = JsonObject()
                                val jsonObject = JSONObject()
                                try {
                                    jsonObject.put("mobileNumber", email)
                                    jsonObject.put("password", reset_pswd.text.toString())
                                    val jsonParser = JsonParser()
                                    loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                                    //print parameter
                                    Log.e("LOGINJSON:", " $loginObj")
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/resetpassword", loginObj, "OTP")
                            }, 1000)
                        } else {
                            ChangepswdResult?.ToastMsg("No Internet Connection!!!!")
                        }
                    } else {
                        reset_pswd.requestFocus()
                        ChangepswdResult?.ToastMsg("Enter confirm password")
                    }
                }
            } else {
                reset_pswd.requestFocus()
                if (from == "menu"){
                    ChangepswdResult?.ToastMsg("Enter new passwoed")
                }else{
                    ChangepswdResult?.ToastMsg("Enter confirm password")
                }
            }
        } else {
            new_pswd.requestFocus()
            if (from == "menu"){
                ChangepswdResult?.ToastMsg("Enter old passwoed")
            }else{
                ChangepswdResult?.ToastMsg("Enter new passwoed")
            }
        }
    }

    override fun successCallBack(body: String?,from:String) {
        val jsonObject: JSONObject?
        try {
            jsonObject = JSONObject(body)
            if (jsonObject.getString("response").equals("3")) {
                if (!fromm.equals(null)) {
                    if (fromm.equals("menu")) {
                        ChangepswdResult?.ToastMsg(jsonObject.getString("message"))
                        ChangepswdResult?.pswdfail()
                    } else {
                        ChangepswdResult?.pswdSuccess(jsonObject.getString("message"))
                    }
                }
            } else {
                ChangepswdResult?.ToastMsg(jsonObject.getString("message"))
            }
            ProgressDialog.getInstance().hideProgress()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        Log.e("changepswd", "failure$failureMsg")
        ChangepswdResult?.ToastMsg(failureMsg.toString())
        ProgressDialog.getInstance().hideProgress()
    }
}