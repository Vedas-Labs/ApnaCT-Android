package com.vedas.apna.Notifications.Adapter

import android.R.color.tertiary_text_light
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Notifications.Model.NotifyInbox
import com.vedas.apna.Notifications.Presenter.NotificationPresenter
import com.vedas.apna.Notifications.View.NotificationView
import com.vedas.apna.R
import java.util.*


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var notifyArray: ArrayList<NotifyInbox>
    private lateinit var notificationPresenter: NotificationPresenter

    fun NotificationAdapter(context: Context, notifyArray: ArrayList<NotifyInbox>,
                            notificationPresenter: NotificationPresenter) {
        this.context = context
        this.notifyArray = notifyArray
        this.notificationPresenter = notificationPresenter
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_notification, null)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_title.text = notifyArray[position].notification.title
        holder.txt_msg.text = notifyArray[position].notification.body
        val cal = Calendar.getInstance(TimeZone.getTimeZone("ASIA/KOLKATA"))
        cal.timeInMillis = notifyArray[position].timeStamp.toLong()
        val date: String = DateFormat.format("dd-MM-yyyy", cal).toString()
        //val time: String = DateFormat.format("hh:mm:ss", cal).toString()
        holder.txt_date.text = date
        if (notifyArray[position].isRead){
            holder.txt_title.setTextColor(ContextCompat.getColor(context, tertiary_text_light))
            holder.txt_msg.setTextColor(ContextCompat.getColor(context,tertiary_text_light))
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NotificationView::class.java)
            val args = Bundle()
            args.putStringArrayList("ARRAYLIST", notifyArray[holder.adapterPosition].data.notifyImages as ArrayList<String>?)
            intent.putExtra("BUNDLE", args)
            intent.putExtra("title", notifyArray[holder.adapterPosition].notification.title)
            intent.putExtra("body", notifyArray[holder.adapterPosition].notification.body)
            intent.putExtra("time", notifyArray[holder.adapterPosition].timeStamp.toString())
            intent.putExtra("id", notifyArray[holder.adapterPosition].data.notifyID.toString())
            intent.putExtra("from", "foreground")
            context.startActivity(intent)
        }
    }

    fun callDeleteFunction(position: Int) {
        notificationPresenter.deleteNotification(context,
                context.getSharedPreferences("LoginPref", 0).getString("mobileNumber", null).toString(),
                notifyArray[position].data.notifyID, "delete")
    }
    fun callDeleteposition(position: String) {
        for (i in 0 until this.notifyArray.size) {
            if (this.notifyArray[i].data.notifyID.equals(position)) {
                this.notifyArray.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun getItemCount(): Int {
        return notifyArray.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_msg: TextView = itemView.findViewById(R.id.txt_msg)
        var txt_date: TextView = itemView.findViewById(R.id.txt_date)
        var txt_title: TextView = itemView.findViewById(R.id.txt_title)
    }
}