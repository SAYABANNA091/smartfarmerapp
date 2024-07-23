package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.activity.EmployeeDetailsActivity
import com.soni.activity.HomeActivity


class AttendanceEmployeeAdapterShimmer(val context: Context):
    RecyclerView.Adapter<AttendanceEmployeeAdapterShimmer.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val ll_base = itemView.findViewById<LinearLayout>(R.id.ll_base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_attendance_employee_shimmer,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  4
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {


    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}