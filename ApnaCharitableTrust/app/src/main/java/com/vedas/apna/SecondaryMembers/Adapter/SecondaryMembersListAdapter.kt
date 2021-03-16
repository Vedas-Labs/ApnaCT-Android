package com.vedas.apna.SecondaryMembers.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vedas.apna.R
import com.vedas.apna.SecondaryMembers.Model.SecondaryList
import com.vedas.apna.ServerConnections.ServerApiCollection.BASE_URL1
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class SecondaryMembersListAdapter : RecyclerView.Adapter<SecondaryMembersListAdapter.ViewHolder>() {
    lateinit var activity: Context
    lateinit var data:ArrayList<SecondaryList>

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.secondary_members_list, null)
        return ViewHolder(layout)
    }

    fun SecondaryMembersListAdapter(context: Context,secondaryList:ArrayList<SecondaryList>) {
        this.activity = context
        this.data = secondaryList
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = "Name: "+data[position].fullName
        holder.memberId.text = "Membership ID: "+data[position].mobileNumber
        Glide.with(activity)
                .load(BASE_URL1+data[position].image)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.pic)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("ASIA/KOLKATA"))
        cal.timeInMillis = data[position].registredAt!!.toLong()
        val date: String = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
        //val time: String = DateFormat.format("hh:mm:ss", cal).toString()
        holder.dateAndTime.text = "Join Date and Time: $date"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (data.size > 0){
            data.size
        }else{
            0
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var dateAndTime: TextView = v.findViewById(R.id.dateAndTime)
        var memberId: TextView = v.findViewById(R.id.memberID)
        var name: TextView = v.findViewById(R.id.memberName)
        var pic: CircleImageView = v.findViewById(R.id.pic)

    }
}