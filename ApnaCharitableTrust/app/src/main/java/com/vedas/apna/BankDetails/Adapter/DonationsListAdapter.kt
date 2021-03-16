package com.vedas.apna.BankDetails.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.BankDetails.Model.DirectDonations
import com.vedas.apna.BankDetails.Model.IndirectDonations
import com.vedas.apna.R
import java.util.*

class DonationsListAdapter : RecyclerView.Adapter<DonationsListAdapter.ViewHolder>() {

    lateinit var activity: Context
    lateinit var data:ArrayList<DirectDonations>
    private lateinit var indata:ArrayList<IndirectDonations>
    lateinit var from: String

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.donation_list, null)
        return ViewHolder(layout)
    }

    fun DonationsListAdapter(
            context: Context,
            donationsList: ArrayList<DirectDonations>,
            indonationsList: ArrayList<IndirectDonations>,
            from: String
    ) {
        this.activity = context
        this.data = donationsList
        this.indata = indonationsList
        this.from= from
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (from == "Direct"){
            holder.membershipId.visibility = View.GONE
            holder.sharedAmount.text =
                "Shared Amount(20%) :" + data[position].directSharedAmount.toString()
            holder.donarName.text = "Donar Name :" + data[position].DonarName
            holder.donationAmount.text = "Donation Amount :" + data[position].DonationAmount.toString()
            val cal = Calendar.getInstance(TimeZone.getTimeZone("ASIA/KOLKATA"))
            cal.timeInMillis = data[position].payDate.toLong()
            val date: String = DateFormat.format("dd-MM-yyyy", cal).toString()
            val time: String = DateFormat.format("hh:mm:ss", cal).toString()
            holder.date.text = "Date :$date"
            holder.time.text = "Time :$time"
        }else{
            holder.membershipId.visibility = View.VISIBLE
            holder.membershipId.text = "Membership ID :" + indata[position].membershipID
            holder.sharedAmount.text =
                "Shared Amount(5%) :" + indata[position].inDirectSharedAmount.toString()
            holder.donarName.text = "Donar Name :" + indata[position].DonarName
            holder.donationAmount.text =
                "Donation Amount :" + indata[position].DonationAmount.toString()

            val cal = Calendar.getInstance(TimeZone.getTimeZone("ASIA/KOLKATA"))
            cal.timeInMillis = indata[position].payDate.toLong()
            val date: String = DateFormat.format("dd-MM-yyyy", cal).toString()
            val time: String = DateFormat.format("hh:mm:ss", cal).toString()
            holder.date.text = "Date :$date"
            holder.time.text = "Time :$time"
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (from == "Direct") {
            if (data.size>0) {
                data.size
            }else{
                0
            }
        }else{
            if (indata.size>0) {
                indata.size
            }else{
                0
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var membershipId: TextView = v.findViewById(R.id.membershipId)
        var donarName: TextView = v.findViewById(R.id.donarName)
        var donationAmount: TextView = v.findViewById(R.id.donationAmount)
        var date: TextView = v.findViewById(R.id.date)
        var time: TextView = v.findViewById(R.id.time)
        var sharedAmount: TextView = v.findViewById(R.id.sharedAmount)
    }
}