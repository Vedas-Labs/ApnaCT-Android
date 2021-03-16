package com.vedas.apna.BankDetails.View

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.vedas.apna.BankDetails.Model.DirectDonations
import com.vedas.apna.BankDetails.Model.IndirectDonations
import com.vedas.apna.BankDetails.Presenter.BankPresenter
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.R
import com.vedas.apna.SecondaryMembers.View.SecondaryMembersActivity
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList


class UploadBankDetails:AppCompatActivity(),IBankView {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var rl_edit: RelativeLayout
    private lateinit var rl_month: RelativeLayout
    private lateinit var img_pic: CircleImageView
    private lateinit var txt_title: TextView
    private lateinit var txt_circle: TextView
    private lateinit var btn_cancel: Button
    private lateinit var btn_Perivous: Button
    private lateinit var btn_secondary: Button
    private lateinit var direct: Button
    private lateinit var indirect: Button
    private lateinit var btn_month: Button
    private lateinit var bankPresenter: BankPresenter
    lateinit var pref : SharedPreferences
    var mobile:String = ""
    var profilepic:String=""
    var name:String=""
    var accesstoken:String=""
    var email:String=""
    private var isBankUpdated:Boolean=false

    private var directDonations: ArrayList<DirectDonations> = ArrayList()
    private var inDirectDonations: ArrayList<IndirectDonations> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        init()
        //fetchBankDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchBankDetails() {
        val year=Calendar.getInstance().get(Calendar.YEAR)
        val month= Calendar.getInstance().get(Calendar.MONTH)+1
        val monthName=Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val currentMonthYear: IntArray = intArrayOf(month, year)
        btn_month.text= "$monthName - $year"

        bankPresenter = BankPresenter()
        bankPresenter.BankPresenter(this, this@UploadBankDetails)
        RetrofitCallbacks.getInstace().initializeServerInterface(bankPresenter)
        bankPresenter.fetchDonationList(currentMonthYear, mobile)
        bankPresenter.fetchBankDetail(this, mobile)
    }

    private fun init() {
        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        isBankUpdated = pref.getBoolean("isBankUpdated", false)
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        rl_edit = findViewById(R.id.rl_edit)
        btn_cancel = findViewById(R.id.btn_cancel)
        btn_Perivous = findViewById(R.id.btn_Perivous)
        btn_secondary = findViewById(R.id.btn_secondary)
        btn_month = findViewById(R.id.btn_month)
        indirect = findViewById(R.id.indirect)
        direct = findViewById(R.id.direct)
        rl_month = findViewById(R.id.rl_month)
        img_pic = findViewById(R.id.img_pic)
        txt_title = findViewById(R.id.txt_title)
        txt_circle = findViewById(R.id.txt_circle)
        UniversalImageLoader.setImage(
                profilepic.substring(1),
                img_pic,
                null,
                ServerApiCollection.BASE_URL1
        )
        txt_title.text = name

        if(isBankUpdated){
            txt_circle.visibility = View.GONE
            rl_edit.visibility = View.VISIBLE
            rl_month.visibility = View.VISIBLE
        }else{
            txt_circle.visibility = View.VISIBLE
            rl_edit.visibility = View.GONE
            rl_month.visibility = View.GONE
        }

        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        btn_cancel.setOnClickListener(rl_backclick)
        txt_circle.setOnClickListener(txt_circleclick)
        rl_edit.setOnClickListener(rl_editclick)
        btn_Perivous.setOnClickListener(btn_Perivousclick)
        direct.setOnClickListener(btn_direct)
        indirect.setOnClickListener(btn_Indirect)
        btn_secondary.setOnClickListener(btn_second)
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
    private val txt_circleclick = View.OnClickListener {
        startActivity(Intent(this@UploadBankDetails, InsertUpdateActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra("from", "upload"))
    }
    private val rl_editclick = View.OnClickListener {
        startActivity(Intent(this@UploadBankDetails, InsertUpdateActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra("from", "edit"))
    }
    private val btn_Perivousclick = View.OnClickListener {
        bankPresenter.calenderPicker()
    }

    private val btn_direct = View.OnClickListener {
        val intent = Intent(this@UploadBankDetails, DonationActivity::class.java)
        val args = Bundle()
        args.putSerializable("ARRAYLIST", directDonations)
        intent.putExtra("BUNDLE", args)
        intent.putExtra("month&year",btn_month.text.toString())
        intent.putExtra("from","Direct")
        startActivity(intent)
    }

    private val btn_Indirect = View.OnClickListener {
        val intent = Intent(this@UploadBankDetails, DonationActivity::class.java)
        val args = Bundle()
        args.putSerializable("ARRAYLIST", inDirectDonations)
        intent.putExtra("BUNDLE", args)
        intent.putExtra("month&year",btn_month.text.toString())
        intent.putExtra("from","Indirect")
        startActivity(intent)
    }

    private val btn_second = View.OnClickListener {
        startActivity(Intent(this@UploadBankDetails, SecondaryMembersActivity::class.java))
    }
    private fun updateViews(monthOfYear: Int, year: Int) {
        val monthYearArray: IntArray = intArrayOf(monthOfYear + 1, year)
        bankPresenter.fetchDonationList(monthYearArray, mobile)

        val month: String = DateFormatSymbols().months[monthOfYear]
        btn_month.text = String.format("%s - %s", month, year)
    }

    override fun onResume() {
        super.onResume()
        init()
        if (isBankUpdated) {
            if(btn_secondary.visibility==View.VISIBLE){
                fetchBankDetails()
            }
        }
    }

    override fun bankresult(Success: String) {

    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun dialogCalender(dialog: MonthYearPickerDialogFragment) {
        dialog.show(supportFragmentManager, null)
        dialog.setOnDateSetListener { year, monthOfYear -> updateViews(monthOfYear, year) }
    }

    @SuppressLint("SetTextI18n")
    override fun donationAmounts(
            directAmount: String,
            IndirectAmount: String,
            directDonations: List<DirectDonations>,
            inDirectDonations: List<IndirectDonations>
    ) {
        direct.text ="Direct Donation 20% Receives RS.$directAmount"
        indirect.text="InDirect Donation 5% Receives RS.$IndirectAmount"
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month: String = DateFormatSymbols().months[c[Calendar.MONTH]]
        Log.e("month&year", " $month - $year")
        if(btn_month.text.toString() != "$month - $year") {
            //btn_Perivous.visibility = View.GONE
            btn_secondary.visibility = View.GONE
        }else{
            btn_secondary.visibility=View.VISIBLE
            //btn_Perivous.visibility=View.VISIBLE
        }
        this.directDonations = ArrayList(directDonations)
        this.inDirectDonations = ArrayList(inDirectDonations)
    }

    override fun onBackPressed() {
        if(btn_secondary.visibility!=View.VISIBLE){
            btn_secondary.visibility=View.VISIBLE
            //btn_Perivous.visibility=View.VISIBLE
            onResume()
        }else{
            super.onBackPressed()
        }
    }
}