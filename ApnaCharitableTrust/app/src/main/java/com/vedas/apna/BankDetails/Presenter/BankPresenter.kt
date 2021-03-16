@file:Suppress("FunctionName")

package com.vedas.apna.BankDetails.Presenter

import android.content.Context
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.github.dewinjm.monthyearpicker.MonthFormat
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vedas.apna.BankDetails.Model.DonationsListResponse
import com.vedas.apna.BankDetails.Model.UploadBankModel
import com.vedas.apna.BankDetails.View.IBankView
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BankPresenter:IBankPresenter,RetrofitCallbacks.ServerResponseInterface {
    private lateinit var bankview: IBankView
    lateinit var context: Context

    fun BankPresenter(bankview: IBankView, context: Context) {
        this.bankview = bankview
        this.context = context
    }

    fun fetchDonationList(array: IntArray, mobile: String) {
        if (AppStatus.getInstance(context).isConnected()) {
            var fetchDonationListObj = JsonObject()
            val jsonObject = JSONObject()
            val jsonArray= JSONArray()

            jsonArray.put(array[0])
            jsonArray.put(array[1])
            try {
                jsonObject.put("mobileNumber", mobile)
                jsonObject.put("monthDate", jsonArray.toString())
                val jsonParser = JsonParser()
                fetchDonationListObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                //print parameter
                Log.e("FetchDonationListReq:", " $fetchDonationListObj")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitCallbacks.getInstace().ApiCallbacks(context, "payments/monthwisedonations", fetchDonationListObj, "fetchDonationList")
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun doupload(context: Context, name: EditText, acNumber: EditText, acNumber2: EditText, bankName: AutoCompleteTextView, branchName: EditText, ifsCode: EditText, villageMandel: EditText, dist: EditText, state: EditText, /*email: EditText, */type: String, mobile: String) {
        this.context = context
        if (name.length()>0){
            if (acNumber.length()>0){
                if (acNumber2.length()>0){
                    if (acNumber2.text.toString() == acNumber.text.toString()){
                        if (bankName.length()>0){
                            if (branchName.length()>0){
                                if (ifsCode.length()>0){
                                    if (isIfscCodeValid(ifsCode.text.toString())){
                                        if (villageMandel.length()>0){
                                            if (dist.length()>0){
                                                if (state.length()>0){
    //                                                if (email.length()>0 && validEmail(email.getText().toString())){
                                                        if (AppStatus.getInstance(context).isConnected()) {
                                                            ProgressDialog.getInstance().showProgress(context)
                                                            upload_editApi(
                                                                    context,
                                                                    name.text.toString(),
                                                                    acNumber2.text.toString(),
                                                                    bankName.text.toString(),
                                                                    branchName.text.toString(),
                                                                    ifsCode.text.toString(),
                                                                    villageMandel.text.toString(),
                                                                    dist.text.toString(),
                                                                    state.text.toString(),
                                                                    /*email.getText().toString(),*/
                                                                    type,mobile
                                                            )
                                                        } else {
                                                            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
                                                        }
      /*                                              }else {
                                                        email.requestFocus()
                                                        bankview.ToastMsg("Enter a email or check valid email")
                                                    }*/
                                                }else{
                                                    state.requestFocus()
                                                    bankview.ToastMsg("Enter a state")
                                                }
                                            }else{
                                                dist.requestFocus()
                                                bankview.ToastMsg("Enter a district")
                                            }
                                        }else{
                                            villageMandel.requestFocus()
                                            bankview.ToastMsg("Enter a village")
                                        }
                                    }else{
                                        ifsCode.requestFocus()
                                        bankview.ToastMsg("Enter valid IFSC Code")
                                    }
                                }else{
                                    ifsCode.requestFocus()
                                    bankview.ToastMsg("Enter IFSC Code")
                                }
                            }else{
                                branchName.requestFocus()
                                bankview.ToastMsg("Enter a branch name")
                            }
                        }else{
                            bankName.requestFocus()
                            bankview.ToastMsg("Enter a bank name")
                        }
                    }else{
                        acNumber2.requestFocus()
                        bankview.ToastMsg("Recheck account number")
                    }
                }else{
                    acNumber2.requestFocus()
                    bankview.ToastMsg("Enter account number")
                }
            }else{
                acNumber.requestFocus()
                bankview.ToastMsg("Enter account number")
            }
        }else{
            name.requestFocus()
            bankview.ToastMsg("Enter a name as per bank")
        }
    }

    override fun fetchBankDetail(context: Context, mobile: String) {
        this.context = context
        if (AppStatus.getInstance(context).isConnected()) {
            var loginObj = JsonObject()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("mobileNumber", mobile)
                val jsonParser = JsonParser()
                loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                //print parameter
                Log.e("LOGINJSON:", " $loginObj")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitCallbacks.getInstace().ApiCallbacks(context, "bank/Fetch", loginObj, "fetchbank")
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun upload_editApi(context: Context, name: String, acNumber: String, bankName: String, branchName: String, ifsc: String, village: String, dist: String, state: String, /*email: String,*/ type: String, mobile: String) {
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobile)
            jsonObject.put("asperBankName", name)
            jsonObject.put("bankAcNumber", acNumber)
            jsonObject.put("bankName", bankName)
            jsonObject.put("branchName", branchName)
            jsonObject.put("ifscCode", ifsc)
            jsonObject.put("VandM", village)
            jsonObject.put("distict", dist)
            jsonObject.put("state", state)
            //jsonObject.put("email", email)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (type == "upload") {
            RetrofitCallbacks.getInstace().ApiCallbacks(
                    context, "bank/insert", loginObj, "upload")
        }else{
            RetrofitCallbacks.getInstace().ApiCallbacks(
                    context, "bank/update", loginObj, "edit")
        }
    }

    fun calenderPicker(){
        val calendar: Calendar = Calendar.getInstance()
        val yearSelected = calendar.get(Calendar.YEAR)
        val monthSelected = calendar.get(Calendar.MONTH)
        calendar.clear()
        calendar.set(2019, 0, 1) // Set minimum date to show in dialog
        val minDate: Long = calendar.timeInMillis // Get milliseconds of the modified date
        calendar.clear()
        calendar.set(yearSelected, monthSelected, calendar.get(Calendar.DATE)) // Set maximum date to show in dialog
        val maxDate: Long = calendar.timeInMillis // Get milliseconds of the modified date
        val monthFormat = MonthFormat.LONG //MonthFormat.LONG or MonthFormat.SHORT
        val dialogFragment =  MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, minDate, maxDate, "Select Month and Year", monthFormat)

        bankview.dialogCalender(dialogFragment)
    }

    /*fun validEmail(target: String?): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return !TextUtils.isEmpty(target) && pattern.matcher(target).matches()
    }*/

    private fun isIfscCodeValid(ifsc: String): Boolean {
        val regExp = "^[A-Z]{4}[0][A-Z0-9]{6}$"
        var isvalid = false
        if (ifsc.isNotEmpty()) {
            isvalid = ifsc.matches(Regex(regExp))
        }
        return isvalid
    }

    override fun successCallBack(body: String?, from: String?) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (jsonObject.has("BankInfo")){
                        val bankModel = UploadBankModel.instance
                        bankModel?.nameasperbank = jsonObject.getJSONObject("BankInfo").getString("asperBankName")
                        bankModel?.accountno = jsonObject.getJSONObject("BankInfo").getString("bankAcNumber")
                        bankModel?.bankName = jsonObject.getJSONObject("BankInfo").getString("bankName")
                        bankModel?.branchName = jsonObject.getJSONObject("BankInfo").getString("branchName")
                        bankModel?.ifsc = jsonObject.getJSONObject("BankInfo").getString("ifscCode")
                        bankModel?.village = jsonObject.getJSONObject("BankInfo").getString("villageOrMandal")
                        bankModel?.dist = jsonObject.getJSONObject("BankInfo").getString("distict")
                        bankModel?.state = jsonObject.getJSONObject("BankInfo").getString("state")
                        //bankModel?.email = jsonObject.getJSONObject("BankInfo").getString("bankMail")
                    }else {
                        when {
                            from.equals("upload") -> {
                                val pref = context.getSharedPreferences("LoginPref", 0) // 0 - for private mode
                                val editor = pref.edit()
                                editor.putBoolean("isBankUpdated", true)
                                editor.apply()
                                bankview.bankresult(from.toString())
                                bankview.ToastMsg(jsonObject.getString("message"))
                            }
                            from.equals("fetchbank") -> {
                                Log.e("ress", ":"+jsonObject.getString("message"))
                                //bankview.ToastMsg(jsonObject.getString("message"))
                                ProgressDialog.getInstance().hideProgress()
                            }
                            from.equals("fetchDonationList") -> {
                                Log.e("ress", ": $body")
                                val gson = Gson()
                                val res: DonationsListResponse = gson.fromJson(body, DonationsListResponse::class.java)
                                Log.e("aaaaaaaa",":aa"+res.results.DirectDonations.size)
                                Log.e("aaaaaaaa",":bbbb"+res.results.indirectDonations.size)
                                bankview.donationAmounts(res.results.totalDirectDonation,
                                        res.results.totalInDirctDonations,
                                    res.results.DirectDonations, res.results.indirectDonations
                                )
                                Log.e("aaaaaaaa",":aaaaa")
                            }
                            else -> {
                                bankview.bankresult(from.toString())
                                bankview.ToastMsg(jsonObject.getString("message"))
                            }
                        }
                    }
                    ProgressDialog.getInstance().hideProgress()
                } else {
                    bankview.ToastMsg(jsonObject.getString("message"))
                    ProgressDialog.getInstance().hideProgress()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        if (failureMsg.equals("No Data Found ... Please pass correct Details ..")) {
            bankview.bankresult("edit")
            bankview.ToastMsg("Member Bank Details Updated sucessfully..")
        }else {
            bankview.ToastMsg(failureMsg.toString())
        }
        ProgressDialog.getInstance().hideProgress()
    }
}