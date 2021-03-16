package com.vedas.apna.Register.Presenter

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Register.View.RegisterView
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RegisterPresenter : IRegisterPresenter, RetrofitCallbacks.ServerResponseInterface {
    private var RegisterResult: RegisterView? = null

    fun RegisterPresenter(iRegResult: RegisterView) {
        this.RegisterResult = iRegResult
    }

    override fun dofetchrefnumber(context: Context, refmobileno: String) {
        if (AppStatus.getInstance(context).isConnected()) {
            Handler().postDelayed({ /*successCallBack("9494956326")*/
                fetchrefApi(
                context,
                refmobileno,
                "fetchmember"
            )
            }, 1000)
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchrefApi(context: Context, refmobileno: String, from: String) {
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", refmobileno)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacks(context, "apna/FetchMember", loginObj, from)
    }

    override fun doRegister(
        context: Context,
        refmobileno: EditText,
        username: EditText,
        email: EditText,
        mobileno: EditText,
        password: EditText,
        retypepswd: EditText,
        image: String
    ) {
        if (image != "") {
            if (refmobileno.text.toString().isNotEmpty()) {
                if (username.text.toString().isNotEmpty()) {
                    if (email.text.toString().isNotEmpty() && validEmail(
                            email.text.toString()
                        )) {
                        if (mobileno.text.toString().isNotEmpty()) {
                            if (password.text.toString().isNotEmpty()) {
                                if (retypepswd.text.toString().isNotEmpty()) {
                                    if (retypepswd.text.toString() == password.text.toString()) {
                                       /* RegisterResult?.registerSuccess("Register Successfully")
                                        RegisterResult?.ToastMsg("Register Successfully")*/
                                        if (AppStatus.getInstance(context).isConnected()) {
                                            ProgressDialog.getInstance().showProgress(context)
                                        registerApi(
                                            context,
                                            refmobileno.text.toString(),
                                            username.text.toString(),
                                            email.text.toString(),
                                            mobileno.text.toString(),
                                            password.text.toString(),
                                            image
                                        )
                                        } else {
                                            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        retypepswd.requestFocus()
                                        RegisterResult?.ToastMsg("Password is not matched!!!")
                                    }
                                } else {
                                    retypepswd.requestFocus()
                                    RegisterResult?.ToastMsg("Retype Password is empty")
                                }
                            } else {
                                password.requestFocus()
                                RegisterResult?.ToastMsg("Password is empty")
                            }
                        } else {
                            mobileno.requestFocus()
                            RegisterResult?.ToastMsg("Mobilenumber is empty")
                        }
                    } else {
                        email.requestFocus()
                        RegisterResult?.ToastMsg("Email is empty")
                    }
                } else {
                    username.requestFocus()
                    RegisterResult?.ToastMsg("Name is empty")
                }
            } else {
                refmobileno.requestFocus()
                RegisterResult?.ToastMsg("Reference Membership ID is empty")
            }
        }else{
            RegisterResult?.ToastMsg("Profile picture is mandatory!!!")
        }
    }

    private fun validEmail(target: String?): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return !TextUtils.isEmpty(target) && pattern.matcher(target).matches()
    }

    private fun registerApi(
        context: Context, refmobileno: String, username: String, email: String,
        mobileno: String, password: String, image: String
    ) {
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileno)
            jsonObject.put("fullName", username)
            jsonObject.put("password", password)
            jsonObject.put("emailID", email)
            jsonObject.put("referalMailID", refmobileno)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacksProfile(
            context, "apna/register", loginObj.toString(),
            File(image), "register"
        )
    }

    override fun successCallBack(body: String?,from: String) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (jsonObject.has("results")){
                        RegisterResult?.fetchrefnumber(jsonObject.getJSONObject("results").getString("image"))
                    }else{
                        RegisterResult?.registerSuccess(jsonObject.getString("message"))
                        ProgressDialog.getInstance().hideProgress()
                    }
                } else {
                    if (from == "fetchmember"){
                        RegisterResult?.registerfail()
                    }else{
                        ProgressDialog.getInstance().hideProgress()
                        RegisterResult?.ToastMsg(jsonObject.getString("message"))
                    }
                }
            }
            /*if (jsonObject.getString("status").equals("3")) {
                RegisterResult?.registerSuccess(jsonObject.getString("message"))
            } else {
                RegisterResult?.ToastMsg(jsonObject.getString("message"))
            }*/
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        ProgressDialog.getInstance().hideProgress()
        RegisterResult?.registerfail()
        RegisterResult?.ToastMsg(failureMsg.toString())
    }
}