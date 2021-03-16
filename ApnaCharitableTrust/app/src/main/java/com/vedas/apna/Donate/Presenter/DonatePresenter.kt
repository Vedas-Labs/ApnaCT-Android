package com.vedas.apna.Donate.Presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import com.vedas.apna.Donate.View.IDonateView
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class DonatePresenter : IDonatePresenter, RetrofitCallbacks.ServerResponseInterface {
    lateinit var iDonateView: IDonateView
    private lateinit var mobileno: String
    lateinit var context: Context

    //private var midString = "wHYABE66288761549414" //TODO testing
    private var midString = "ZVObax35237045796579" //TODO playstore
    private lateinit var txnAmountString: String
    private lateinit var orderIdString: String
    private lateinit var txnTokenString: String
    lateinit var jsonObject: JSONObject
    fun DonatePresenter(iDonateView: IDonateView, mobileno: String) {
        this.iDonateView = iDonateView
        this.mobileno = mobileno
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

    override fun dosubmit(
        context: Context,
        mobile: EditText,
        dName: EditText,
        dMobile: EditText,
        pan: EditText,
        emailid: EditText,
        address: EditText,
        dAmount: EditText,
        tran_date: EditText,
        spin_deposit: Spinner,
        spin: Spinner,
        files: ArrayList<File>,
        type: String
    ) {
        this.context = context
        if (mobile.length() > 0) {
            if (mobile.text.toString().length == 10) {
                if (dName.length() > 0) {
                    if (dMobile.length() > 0) {
                        if (dMobile.text.toString().length == 10) {
                            if (pan.length() > 0) {
                                if (isValidPan(pan.text.toString())) {
                                    if (emailid.length() > 0) {
                                        if (validEmail(emailid.text.toString())) {
                                            if (address.length() > 0) {
                                                if (dAmount.length() > 0
                                                        && Integer.parseInt(dAmount.text.toString())>0
                                                        && Integer.parseInt(dAmount.text.toString()) <= 2000) {
                                                    if (type == "online") {
                                                        if (!spin.selectedItem.toString()
                                                                .contentEquals(
                                                                    "Select any one"
                                                                )
                                                        ) {
                                                            if (AppStatus.getInstance(context)
                                                                    .isConnected()
                                                            ) {
                                                                ProgressDialog.getInstance()
                                                                    .showProgress(
                                                                        context
                                                                    )
                                                                onlineApifun(
                                                                    context,
                                                                    mobile.text.toString(),
                                                                    dName.text.toString(),
                                                                    dMobile.text.toString(),
                                                                    pan.text.toString(),
                                                                    emailid.text.toString(),
                                                                    address.text.toString(),
                                                                    dAmount.text.toString(),
                                                                    spin.selectedItem.toString(),
                                                                    type
                                                                )
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "No Internet Connection!!!!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        } else {
                                                            spin.isFocusable = true
                                                            spin.isFocusableInTouchMode = true
                                                            spin.requestFocus()
                                                            iDonateView.ToastMsg("select any one you liked to donation amount spent")
                                                        }
                                                    } else {
                                                        if (!spin_deposit.selectedItem.toString().contentEquals("Select any one")) {
                                                            if (!spin.selectedItem.toString().contentEquals("Select any one")) {
                                                                if (files.size > 0) {
                                                                if (AppStatus.getInstance(context).isConnected()) {
                                                                    ProgressDialog.getInstance().showProgress(context)
                                                                    offlineApifun(
                                                                        context,
                                                                        mobile.text.toString(),
                                                                        dName.text.toString(),
                                                                        dMobile.text.toString(),
                                                                        pan.text.toString(),
                                                                        emailid.text.toString(),
                                                                        address.text.toString(),
                                                                        dAmount.text.toString(),
                                                                        spin_deposit.selectedItem.toString(),
                                                                        spin.selectedItem.toString(),
                                                                        files,
                                                                        type
                                                                    )
                                                                } else {
                                                                    Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
                                                                }
                                                                } else {
                                                                    iDonateView.ToastMsg("Upload any recepit")
                                                                }
                                                            } else {
                                                                spin.isFocusable = true
                                                                spin.isFocusableInTouchMode = true
                                                                spin.requestFocus()
                                                                iDonateView.ToastMsg("select any one you liked to donation amount spent ")
                                                            }
                                                        } else {
                                                            spin_deposit.isFocusable = true
                                                            spin_deposit.isFocusableInTouchMode =
                                                                true
                                                            spin_deposit.requestFocus()
                                                            iDonateView.ToastMsg("select any one deposit type")
                                                        }
                                                    }
                                                } else {
                                                    dAmount.requestFocus()
                                                    iDonateView.ToastMsg("Please check donation amount")
                                                }
                                            } else {
                                                address.requestFocus()
                                                iDonateView.ToastMsg("Enter address")
                                            }
                                        } else {
                                            emailid.requestFocus()
                                            iDonateView.ToastMsg("Enter valid emailid.")
                                        }
                                    } else {
                                        emailid.requestFocus()
                                        iDonateView.ToastMsg("Enter emailid.")
                                    }
                                } else {
                                    pan.requestFocus()
                                    iDonateView.ToastMsg("Enter valid Pan number.")
                                }
                            } else {
                                pan.requestFocus()
                                iDonateView.ToastMsg("Enter Pan number.")
                            }
                        } else {
                            dMobile.requestFocus()
                            iDonateView.ToastMsg("Donator Mobile number is 10 digits only!!")
                        }
                    } else {
                        dMobile.requestFocus()
                        iDonateView.ToastMsg("Donator Mobile number is empty")
                    }
                } else {
                    dName.requestFocus()
                    iDonateView.ToastMsg("Enter Donator Name.")
                }
            } else {
                mobile.requestFocus()
                iDonateView.ToastMsg("Reference Membership ID is 10 digits only!!")
            }
        } else {
            mobile.requestFocus()
            iDonateView.ToastMsg("Reference Membership ID is empty")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun offlineApifun(
        context: Context,
        mobile: String,
        dName: String,
        dMobile: String,
        pan: String,
        emailid: String,
        address: String,
        dAmount: String,
        spin_deposit: String,
        spentto: String,
        files: ArrayList<File>,
        type: String
    ) {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("ddMMyyyy")
        val date: String = df.format(c.time)
        val rand = Random()
        val min = 1000
        val max = 9999
// nextInt as provided by Random is exclusive of the top value so you need to add 1
        // nextInt as provided by Random is exclusive of the top value so you need to add 1
        val randomNum = rand.nextInt(max - min + 1) + min
        orderIdString = "ORDERID_$date$randomNum"
        /*  this.dAmount = dAmount*/
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobile)
            jsonObject.put("donarMobileNumer", dMobile)
            jsonObject.put("donarName", dName)
            jsonObject.put("donarPancard", pan)
            jsonObject.put("donarEmailID", emailid)
            jsonObject.put("donarAddress", address)
            jsonObject.put("donationAmount", dAmount)
            jsonObject.put("donationSpend", spentto)
            jsonObject.put("paymentMode", spin_deposit)
            jsonObject.put("paymentType", type)
            jsonObject.put("orderId", orderIdString)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacksOffline(
            context, "payments/offlinepayment", loginObj, files, type
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun onlineApifun(
        context: Context,
        mobile: String,
        dName: String,
        dMobile: String,
        pan: String,
        emailid: String,
        address: String,
        dAmount: String,
        spentto: String,
        type: String
    ) {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("ddMMyyyy")
        val date: String = df.format(c.time)
        val rand = Random()
        val min = 1000
        val max = 9999
// nextInt as provided by Random is exclusive of the top value so you need to add 1
        // nextInt as provided by Random is exclusive of the top value so you need to add 1
        val randomNum = rand.nextInt(max - min + 1) + min
        orderIdString = "ORDERID_$date$randomNum"
        this.txnAmountString = dAmount  //TODO Playstore update = dAmount  and   local update = "1"
        //this.txnAmountString = "1"
        var loginObj = JsonObject()
        jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobile)
            jsonObject.put("donarMobileNumer", dMobile)
            jsonObject.put("channelId", "APP")
            jsonObject.put("orderId", orderIdString)
            jsonObject.put("donarName", dName)
            jsonObject.put("donarPancard", pan)
            jsonObject.put("donarEmailID", emailid)
            jsonObject.put("donarAddress", address)
            jsonObject.put("donationAmount", txnAmountString)
            jsonObject.put("donationSpend", spentto)
            jsonObject.put("paymentMode", type)
            jsonObject.put("paymentType", type)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //startActivityForResult(context as Activity, Intent(context,DonateActivity::class.java), 3,Bundle())
        RetrofitCallbacks.getInstace().ApiCallbacks(
            context, "payments/getPaytmTxnToken", loginObj, type
        )
    }

    private fun isValidPan(pan: String?): Boolean {
        val mPattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]")
        val mMatcher: Matcher = mPattern.matcher(pan)
        return mMatcher.matches()
    }

    private fun validEmail(target: String?): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return !TextUtils.isEmpty(target) && pattern.matcher(target).matches()
    }

    override fun successCallBack(body: String?, from: String?) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.has("Tokenval")) {
                    Log.e("response Tokenval ", "messsage$body")
                    if (AppStatus.getInstance(context).isConnected()) {
                        ProgressDialog.getInstance().hideProgress()
                        startPaytmPayment(jsonObject.getJSONObject("Tokenval").getJSONObject("body").getString("txnToken"))
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                        ProgressDialog.getInstance().hideProgress()
                    }
                } else {
                    if (jsonObject.getString("response").equals("3")) {
                        if (jsonObject.has("results")) {
                            iDonateView.fetchrefnumber(
                                jsonObject.getJSONObject("results").getString("image")
                            )
                        } else {
                            if (from.equals("online")) {
                                iDonateView.donateSuccess(jsonObject.getString("message"))
                                iDonateView.ToastMsg(jsonObject.getString("message"))
                                ProgressDialog.getInstance().hideProgress()
                            } else if (from.equals("offline")) {
                                //iDonateView.offlineSuccess()
                                SuccessAlertDialog(context)
                                //iDonateView.ToastMsg(jsonObject.getString("message"))
                                ProgressDialog.getInstance().hideProgress()
                            }
                        }
                    } else {
                        if (from.equals("fetchmember")) {
                            iDonateView.donatefail()
                        } else {
                            ProgressDialog.getInstance().hideProgress()
                            iDonateView.ToastMsg(jsonObject.getString("message"))
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    private fun startPaytmPayment(Token: String) {
        Log.e("response Tokenval ", "messsageeee")
        txnTokenString = Token
        // for test mode use it
        //val host = "https://securegw-stage.paytm.in/";
        // for production mode use it
        val host = "https://securegw.paytm.in/"
        val orderDetails =
                "MID: $midString, OrderId: $orderIdString, TxnToken: $txnTokenString, Amount: $txnAmountString"
        Log.e("order ", "order details $orderDetails")
        //Log.e(TAG, "order details "+ orderDetails);
        val callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + orderIdString
        Log.e("callback", " callback URL $callBackUrl")
        val paytmOrder = PaytmOrder(
            orderIdString,
            midString,
            txnTokenString,
            txnAmountString,
            callBackUrl
        )
        val transactionManager = TransactionManager(
            paytmOrder,
            object : PaytmPaymentTransactionCallback {
                override fun onTransactionResponse(bundle: Bundle) {
                    Log.e("paytm_Responseee", "Response (onTransactionResponse) : $bundle")

                    val txnStatus: String = bundle.getString("STATUS").toString()
                    val txnId: String = bundle.getString("TXNID").toString()
                    val orderId: String = bundle.getString("ORDERID").toString()
                    if (txnStatus == "TXN_SUCCESS") {
                        donationConfirmation(
                            context,
                            txnStatus,
                            txnId,
                        )
                    }
                }

                override fun networkNotAvailable() {
                    Log.e("paytmnetwork", "network not available ")
                }

                override fun onErrorProceed(s: String) {
                    Log.e("onError", " onErrorProcess $s")
                }

                override fun clientAuthenticationFailed(s: String) {
                    Log.e("clientAuthentication", "Clientauth $s")
                }

                override fun someUIErrorOccurred(s: String) {
                    Log.e("someUIError", " UI error $s")
                }

                override fun onErrorLoadingWebPage(i: Int, s: String, s1: String) {
                    Log.e("onErrorLoadingWebPage", " error loading web $s--$s1")
                    ProgressDialog.getInstance().hideProgress()
                    iDonateView.ToastMsg(" error loading web $s--$s1")
                }

                override fun onBackPressedCancelTransaction() {
                    Log.e("onBackPressed", "backPress ")
                }

                override fun onTransactionCancel(s: String, bundle: Bundle) {
                    Log.e("onTransactionCancel", " transaction cancel $s")
                    ProgressDialog.getInstance().hideProgress()
                    iDonateView.ToastMsg(s)
                }
            })

        //transactionManager.setRedirectionEnabled(true)
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage")
        transactionManager.startTransaction(context as Activity, 3)
        //transactionManager.setSubscriptioFlow(true)
    }

    override fun failureCallBack(failureMsg: String?) {
        iDonateView.donatefail()
        iDonateView.ToastMsg(failureMsg.toString())
        ProgressDialog.getInstance().hideProgress()
    }

    fun donationConfirmation(
        context: Context,
        txnStatus: String,
        txnId: String
    ) {
        if (AppStatus.getInstance(
                context
            ).isConnected()
        ) {
            ProgressDialog.getInstance().showProgress(context)
            var loginObj = JsonObject()
            val jsonObject = jsonObject
            try {
                jsonObject.put("STATUS", txnStatus)
                jsonObject.put("TXNID", txnId)
                val jsonParser = JsonParser()
                loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
                //print parameter
                Log.e("LOGINJSON:", " $loginObj")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitCallbacks.getInstace().ApiCallbacks(context, "payments/onlinepayment", loginObj, "online")
        } else {
            Toast.makeText(
                context,
                "No Internet Connection!!!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun SuccessAlertDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.offline_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val ok = dialog.findViewById<Button>(R.id.ok)
        ok.setOnClickListener {
            dialog.dismiss()
            finishAffinity(context as Activity)
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
        dialog.show()
    }
}