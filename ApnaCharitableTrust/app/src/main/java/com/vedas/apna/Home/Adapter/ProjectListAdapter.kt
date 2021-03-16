package com.vedas.apna.Home.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Home.MainProjectsModel
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.R


internal class ProjectListAdapter(
    context: Context,
    mainProjectsModels: List<MainProjectsModel>,
    mainProjectRecyclerView: RecyclerView
) :
    RecyclerView.Adapter<ProjectListAdapter.MyPromoHolder>() {
    var context: Context? = null
    private var mainProjectsModels: List<MainProjectsModel> = ArrayList()
    private var homeActivity: HomeActivity = context as HomeActivity
    private var mainProjectRecyclerView: RecyclerView
    private var selectedPosition = 0
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPromoHolder {
        val layout: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_project_details, null)
        val myholder = MyPromoHolder(layout)
        homeActivity.displayGoals("Education", mainProjectsModels[0].image)
        return myholder
    }

    override fun onBindViewHolder(holder: MyPromoHolder, position: Int) {
        holder.title.text = mainProjectsModels[position].title
        if (selectedPosition == position) {
            holder.title.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
            holder.rl_list.background = ContextCompat.getDrawable(homeActivity, R.drawable.button_orange)
            //holder.title.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E19426"))
        } else {
            holder.title.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")))
            holder.rl_list.background = ContextCompat.getDrawable(homeActivity, R.drawable.btn_border_orange)
        }
        holder.title.setOnClickListener {
            selectedPosition = holder.adapterPosition
            homeActivity.displayGoals(
                    holder.title.text.toString(),
                    mainProjectsModels[holder.adapterPosition].image
            )
            mainProjectRecyclerView.scrollToPosition(selectedPosition)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mainProjectsModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyPromoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var rl_list : RelativeLayout = itemView.findViewById(R.id.rl_list)

    }

    init {
        this.mainProjectsModels = mainProjectsModels
        this. mainProjectRecyclerView =  mainProjectRecyclerView
    }
}
