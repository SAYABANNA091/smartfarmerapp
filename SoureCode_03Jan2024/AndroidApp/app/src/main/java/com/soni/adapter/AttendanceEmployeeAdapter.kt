package com.soni.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.EmployeeDetailsActivity
import com.soni.activity.HomeActivity
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.EmployeeData
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AttendanceEmployeeAdapter(val context: Context,var list:ArrayList<EmployeeData>,private var onItemClicked: ((item:Boolean) -> Unit )):
    RecyclerView.Adapter<AttendanceEmployeeAdapter.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val ck_employee = itemView.findViewById<CheckBox>(R.id.ck_employee)
        val iv_requests = itemView.findViewById<ImageView>(R.id.iv_requests)
        val ll_base = itemView.findViewById<LinearLayout>(R.id.ll_base)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_attendance_employee_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    public fun onUpdate(){

        notifyDataSetChanged()

    }
    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {

        holder.ll_base.setOnClickListener {
            var mIntent=Intent((context as HomeActivity),EmployeeDetailsActivity::class.java)
            mIntent.putExtra(Const.IntentKey.Employee,list[position])
            context.startActivity(mIntent)
        }
        holder.ck_employee.setOnClickListener {
            list[position].isSelected = holder.ck_employee.isChecked
            var isAll=true
            list.forEach {
                if (it.isAttend==0){
                    isAll=false
                }
            }
            onItemClicked(isAll)
        }


        holder.ck_employee.isChecked= list[position].isSelected!!

        holder.txtNotification.text=list[position].employeeName!!

        if (list[position].profilePicture !=null) {
            if (list[position].profilePicture!!.isNotEmpty()) {
                Glide.with(context as HomeActivity)
                    .load(list[position].profilePicture!!)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.iv_requests)
            }
        }else
        {
            Glide.with(context as HomeActivity)
                .load(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.iv_requests)
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