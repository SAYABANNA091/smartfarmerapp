package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.activity.PropertyDetailActivity
import com.soni.utils.Const


class InquiriesHomeAdapterShimmer(val context: Context,var isProducts:Boolean=false):
    RecyclerView.Adapter<InquiriesHomeAdapterShimmer.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val llBase = itemView.findViewById<LinearLayout>(R.id.ll_base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_inquiries_home_adapter_shimmer,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  10
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        if (isProducts) {
            var base = holder.llBase.layoutParams
            val height =
                context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._75sdp).toInt()
            base.height = height
            holder.llBase.layoutParams = base
        }

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}