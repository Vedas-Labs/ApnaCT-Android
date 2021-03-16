package com.vedas.apna.BankDetails.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.BankDetails.Adapter.DonationsListAdapter
import com.vedas.apna.BankDetails.Model.DirectDonations
import com.vedas.apna.BankDetails.Model.IndirectDonations
import com.vedas.apna.R
import org.json.JSONArray
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class DonationActivity : AppCompatActivity() {

    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var img_download: ImageView
    private lateinit var rl_download: RelativeLayout
    private lateinit var donationRecyclerView: RecyclerView
    lateinit var txt_title: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var directDonations: ArrayList<DirectDonations> = ArrayList()
    private var inDirectDonations: ArrayList<IndirectDonations> = ArrayList()
    lateinit var adapter: DonationsListAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)

        donationRecyclerView = findViewById(R.id.donationListRecycler)
        txt_title = findViewById(R.id.txt_title)
        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        img_download = findViewById(R.id.img_download)
        rl_download = findViewById(R.id.rl_download)
        linearLayoutManager = LinearLayoutManager(this)
        donationRecyclerView.layoutManager = linearLayoutManager
        adapter = DonationsListAdapter()
        val args = intent.getBundleExtra("BUNDLE")
        if (intent.getStringExtra("from").equals("Direct")){
            @Suppress("UNCHECKED_CAST")
            directDonations = args.getSerializable("ARRAYLIST") as ArrayList<DirectDonations>
            inDirectDonations = ArrayList()/*args.getSerializable("ARRAYLIST") as ArrayList<IndirectDonation>*/
        }else{
            directDonations = ArrayList()/*args.getSerializable("ARRAYLIST") as ArrayList<DirectDonation>*/
            @Suppress("UNCHECKED_CAST")
            inDirectDonations = args.getSerializable("ARRAYLIST") as ArrayList<IndirectDonations>
        }
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month: String = DateFormatSymbols().months[c[Calendar.MONTH]]
        Log.e("month&year", " $month - $year")
        if (intent.getStringExtra("month&year").equals("$month - $year")){
            rl_download.visibility = View.GONE
        }else{
            if (intent.getStringExtra("from").equals("Direct")){
                if (directDonations.size>0){
                    rl_download.visibility = View.VISIBLE
                }
            }else{
                if (inDirectDonations.size>0){
                    rl_download.visibility = View.VISIBLE
                }
            }
        }

        if (intent.getStringExtra("from").equals("Direct")){
            txt_title.text="Direct Donations"
            adapter.DonationsListAdapter(this, directDonations, inDirectDonations, intent.getStringExtra("from"))
        }else{
            txt_title.text="Indirect Donations"
            adapter.DonationsListAdapter(this, directDonations, inDirectDonations, intent.getStringExtra("from"))
        }
        donationRecyclerView.adapter = adapter
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        rl_download.setOnClickListener(downloadclick)
        img_download.setOnClickListener(downloadclick)
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
    @SuppressLint("SimpleDateFormat")
    private val downloadclick = View.OnClickListener {
        val mobile: String = getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString()
        val jsonArray= JSONArray()
        var monthNumber = 0
        val str: String= intent.getStringExtra("month&year").toString()
        val namesList: Array<String> = str.split("- ").toTypedArray()
        try {
            val date: Date = SimpleDateFormat("MMMM").parse(namesList[0]) //put your month name here
            val cal = Calendar.getInstance()
            cal.time = date
            monthNumber = (cal[Calendar.MONTH]+1)
            println(monthNumber)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val year:Int = Integer.parseInt(namesList[1])
        Log.e("month", "$monthNumber")
        Log.e("year", "$year")
        jsonArray.put(monthNumber)
        jsonArray.put(year)
        if (intent.getStringExtra("from").equals("Direct")){
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://api.apnacharitabletrust.org/api/payments/downloadPdfFile?mobileNumber=$mobile&monthDate=$jsonArray&downloadType=directDonation"))
            //intent.data = Uri.parse("http://54.167.167.144:3000/api/payments/downloadPdfFile?mobileNumber="+mobile+"&monthDate="+jsonArray.toString()+"&downloadType=directDonation")
            startActivity(intent)

            /*downloadfunction(this@DonationActivity,
                    getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(),
                    intent.getStringExtra("month&year"), "directDonation")*/
        }else{
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://api.apnacharitabletrust.org/api/payments/downloadPdfFile?mobileNumber=$mobile&monthDate=$jsonArray&downloadType=indirectDonation"))
            startActivity(intent)
            /*downloadfunction(this@DonationActivity,
                    getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(),
                    intent.getStringExtra("month&year"), "indirectDonation")*/
        }
       Toast.makeText(this@DonationActivity, "Downloading Pdf...", Toast.LENGTH_SHORT).show()
    }
}