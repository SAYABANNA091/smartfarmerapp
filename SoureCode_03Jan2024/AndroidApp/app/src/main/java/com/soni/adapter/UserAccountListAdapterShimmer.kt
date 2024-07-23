package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.activity.HomeActivity
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const

class UserAccountListAdapterShimmer(val context: Context, var list:ArrayList<PropertyModel>):
    RecyclerView.Adapter<UserAccountListAdapterShimmer.UserAccountListAdapterViewHolder>() {
    class UserAccountListAdapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAccountListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_user_account_list_adapter_shimmer,parent,false)

        return  UserAccountListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: UserAccountListAdapterViewHolder, position: Int) {





    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}