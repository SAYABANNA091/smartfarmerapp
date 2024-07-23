package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.activity.HomeActivity
import com.soni.services.web.models.EmployeeListData
import java.util.ArrayList


class FilterEmployeeAdapter(val context: Context, var viewTabEmployeeList: ArrayList<EmployeeListData>,var callback:((isAll:Boolean)->Unit)):
    RecyclerView.Adapter<FilterEmployeeAdapter.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val iv_requests = itemView.findViewById<ImageView>(R.id.iv_requests)
        val ck_employee = itemView.findViewById<CheckBox>(R.id.ck_employee)
        val ll_user = itemView.findViewById<LinearLayout>(R.id.ll_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_attendance_employee_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  viewTabEmployeeList.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {

        holder.ll_user.background=context.resources.getDrawable(R.drawable.simple_edittext_white100)
        holder.ck_employee.setOnClickListener {
            viewTabEmployeeList[position].isSelected=holder.ck_employee.isClickable
            var isAll=true
            viewTabEmployeeList.forEach {
                if(!it.isSelected){
                    isAll=false
                }
            }
            callback(isAll)
        }
        holder.ck_employee.isChecked=viewTabEmployeeList[position].isSelected
        holder.txtNotification.text=viewTabEmployeeList[position].employeeName!!

        if (viewTabEmployeeList[position].profilePicture !=null) {
            if (viewTabEmployeeList[position].profilePicture!!.isNotEmpty()) {
                Glide.with(context as HomeActivity)
                    .load(viewTabEmployeeList[position].profilePicture!!)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.iv_requests)
            }
        }


        holder.setIsRecyclable(false);
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}