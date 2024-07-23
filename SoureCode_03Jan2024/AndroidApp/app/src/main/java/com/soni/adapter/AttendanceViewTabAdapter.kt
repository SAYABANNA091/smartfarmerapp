package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.activity.HomeActivity
import com.soni.services.web.models.EmployeeAtt
import java.util.ArrayList


class AttendanceViewTabAdapter(val context: Context,var list: ArrayList<EmployeeAtt>):
    RecyclerView.Adapter<AttendanceViewTabAdapter.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txt_name = itemView.findViewById<TextView>(R.id.txt_name)
        val txt_job = itemView.findViewById<TextView>(R.id.txt_job)
        val iv_employee_pic = itemView.findViewById<ImageView>(R.id.iv_employee_pic)
        val txt_days = itemView.findViewById<TextView>(R.id.txt_days)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_attendance_view_tab_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        if (list[position].profilePicture !=null) {
            if (list[position].profilePicture!!.isNotEmpty()) {
                Glide.with(context as HomeActivity)
                    .load(list[position].profilePicture!!)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.iv_employee_pic)
            }
        }else
        {
            Glide.with(context as HomeActivity)
                .load(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.iv_employee_pic)
        }
        holder.txt_days.text=list[position].presentDays.toString()
        holder.txt_job.text=if(list[position].department.toString()=="null"||list[position].department==null) "" else list[position].department.toString()
        holder.txt_name.text=list[position].employeeName.toString()

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}