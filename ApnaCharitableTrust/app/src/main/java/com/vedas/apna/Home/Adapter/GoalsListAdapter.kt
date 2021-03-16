@file:Suppress("CanBePrimaryConstructorProperty", "CanBePrimaryConstructorProperty", "CanBePrimaryConstructorProperty")

package com.vedas.apna.Home.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.R


internal class GoalsListAdapter(context: Context, goalsList: Array<String>) :
    RecyclerView.Adapter<GoalsListAdapter.MyPromoHolder>() {
    var context: Context = context
    private var goalsList: Array<String> = goalsList
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPromoHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.goals_lists, null)
        return MyPromoHolder(layout)
    }

    override fun onBindViewHolder(holder: MyPromoHolder, position: Int) {
        holder.title.text = goalsList[position]
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class MyPromoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
    }
}