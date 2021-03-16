package com.vedas.apna.BankDetails.View

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.vedas.apna.BankDetails.Model.DirectDonations
import com.vedas.apna.BankDetails.Model.IndirectDonations
import com.vedas.apna.BankDetails.Model.UploadBankModel
import com.vedas.apna.BankDetails.Presenter.BankPresenter
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class InsertUpdateActivity : AppCompatActivity(),IBankView {
    private lateinit var nameas: EditText
    private lateinit var acNumber: EditText
    private lateinit var acNumber2: EditText
    private lateinit var bankName: AutoCompleteTextView
    private lateinit var branchName: EditText
    private lateinit var ifsCode: EditText
    private lateinit var villageMandel: EditText
    private lateinit var dist: EditText
    private lateinit var state: EditText
    private lateinit var img_pswd_eye : ImageView
    private lateinit var rl_eye : RelativeLayout
    private lateinit var img_pswd_eye_cnfrm : ImageView
    private lateinit var rl_eye_cnfrm : RelativeLayout
    //lateinit var emailid: EditText
    private lateinit var upload: Button
    lateinit var cancel: Button
    lateinit var rl_back: RelativeLayout
    lateinit var img_back: ImageView
    lateinit var txt_title: TextView
    private lateinit var bankPresenter: BankPresenter
    lateinit var pref : SharedPreferences
    var mobile:String = ""
    var profilepic:String=""
    var name:String=""
    var accesstoken:String=""
    var email:String=""
    private var isBankUpdated:Boolean=false
    private var bankModel = UploadBankModel.instance
    private var eye1:Boolean = false
    private var eye2:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addbankdetails)
        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        isBankUpdated = pref.getBoolean("isBankUpdated", false)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        nameas = findViewById(R.id.name)
        acNumber = findViewById(R.id.acNumber)
        acNumber2 = findViewById(R.id.acNumber2)
        bankName = findViewById(R.id.bankName)
        branchName = findViewById(R.id.branchName)
        ifsCode = findViewById(R.id.ifsCode)
        villageMandel = findViewById(R.id.villageMandel)
        dist = findViewById(R.id.dist)
        state = findViewById(R.id.state)
        /*emailid = findViewById(R.id.email)*/
        upload = findViewById(R.id.upload)
        cancel = findViewById(R.id.cancel)
        rl_back = findViewById(R.id.rl_back)
        img_back = findViewById(R.id.img_back)
        txt_title = findViewById(R.id.txt_title)
        rl_eye = findViewById(R.id.rl_eye)
        img_pswd_eye = findViewById(R.id.img_pswd_eye)
        rl_eye_cnfrm = findViewById(R.id.rl_eye_cfrm)
        img_pswd_eye_cnfrm = findViewById(R.id.img_pswd_eye_cnfrm)
        rl_eye.setOnClickListener(rl_eyeclick)
        rl_eye_cnfrm.setOnClickListener(rl_eye_cnfrmclick)
        val banklists = resources.getStringArray(R.array.Bank_list)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
                R.layout.simple_spinner_dropdown_item, banklists)
        bankName.setAdapter(adapter)
        bankName.threshold = 1
        bankPresenter = BankPresenter()
        bankPresenter.BankPresenter(this, this@InsertUpdateActivity)
        RetrofitCallbacks.getInstace().initializeServerInterface(bankPresenter)

        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        cancel.setOnClickListener(rl_backclick)
        upload.setOnClickListener(uploadclick)
        if (intent.getStringExtra("from").equals("edit")){
            nameas.setText(bankModel?.nameasperbank)
            acNumber.setText(bankModel?.accountno)
            acNumber2.setText(bankModel?.accountno)
            bankName.setText(bankModel?.bankName)
            branchName.setText(bankModel?.branchName)
            ifsCode.setText(bankModel?.ifsc)
            villageMandel.setText(bankModel?.village)
            dist.setText(bankModel?.dist)
            state.setText(bankModel?.state)
            upload.text = "Update"
      //      emailid.setText(bankModel?.email)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_eyeclick = View.OnClickListener {
        if (!eye1){
            acNumber.post { acNumber.setSelection(acNumber.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@InsertUpdateActivity,R.drawable.eye_on))
            acNumber.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye1=true
        }else{
            acNumber.post { acNumber.setSelection(acNumber.length()) }
            img_pswd_eye.setImageDrawable(ContextCompat.getDrawable(this@InsertUpdateActivity,R.drawable.eye_off))
            acNumber.transformationMethod = PasswordTransformationMethod.getInstance()
            eye1=false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_eye_cnfrmclick = View.OnClickListener {
        if (!eye2){
            acNumber2.post { acNumber2.setSelection(acNumber2.length()) }
            img_pswd_eye_cnfrm.setImageDrawable(ContextCompat.getDrawable(this@InsertUpdateActivity,R.drawable.eye_on))
            acNumber2.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye2=true
        }else{
            acNumber2.post { acNumber2.setSelection(acNumber2.length()) }
            img_pswd_eye_cnfrm.setImageDrawable(ContextCompat.getDrawable(this@InsertUpdateActivity,R.drawable.eye_off))
            acNumber2.transformationMethod = PasswordTransformationMethod.getInstance()
            eye2=false
        }
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
    private val uploadclick = View.OnClickListener {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isAcceptingText) {
            Log.e("shown", "")
            imm.hideSoftInputFromWindow(
                    upload.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        } else {
            Log.e("unshown", "")
        }
        if (intent.getStringExtra("from").equals("edit")){
            bankPresenter.doupload(this@InsertUpdateActivity, nameas, acNumber, acNumber2, bankName, branchName, ifsCode, villageMandel, dist, state,/*emailid,*/"edit", mobile)
        }else{
            bankPresenter.doupload(this@InsertUpdateActivity, nameas, acNumber, acNumber2, bankName, branchName, ifsCode, villageMandel, dist, state,/*emailid,*/"upload", mobile)
        }
    }
    /*fun SuccessAlertDialog() {
        val dialog = Dialog(this@InsertUpdateActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.success_popup)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btn_unfriend = dialog.findViewById<RelativeLayout>(R.id.rl_cancel)
        val txt_username = dialog.findViewById<TextView>(R.id.txt_username)
        txt_username.text = "Bank Details Uploaded Successfully!!"
        //txt_username.setOnClickListener { dialog.dismiss() }
        btn_unfriend.setOnClickListener {
            dialog.dismiss()
            finish()
            //startActivity(Intent(this@InsertUpdateActivity, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        dialog.show()
    }*/

    override fun bankresult(Success: String) {
        if (Success == "upload") {
            //SuccessAlertDialog()
            finish()
        }else{
            finish()
        }
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun dialogCalender(dialog: MonthYearPickerDialogFragment) {

    }

    override fun donationAmounts(
            directAmount: String,
            IndirectAmount: String,
            directDonations: List<DirectDonations>,
            inDirectDonations: List<IndirectDonations>
    ) {

    }
}