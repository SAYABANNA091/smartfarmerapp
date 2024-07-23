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
import com.soni.activity.PropertyDetailActivity
import com.soni.utils.Const
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BuySelItemListAdapterShimmer(val context: Context,var isFromFav:Boolean=false):
    RecyclerView.Adapter<BuySelItemListAdapterShimmer.InquiriesHomeAdapterViewHoldes>() {

    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

//        val tv_type = itemView.findViewById<TextView>(R.id.tv_type)
        val base = itemView.findViewById<LinearLayout>(R.id.ll_root)
        val ll_root = itemView.findViewById<LinearLayout>(R.id.ll_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_buy_sell_item_list_adapter_shimmer,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  if (isFromFav)7 else 4
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
//        holder.tv_type.text =types[position]
//        if (position!=0){
//            holder.base.background=context.getDrawable(R.drawable.account_5sdp_radius)
//            holder.tv_type.setTextColor(context.getColor(R.color.Primary100))
//
//        }
        if(isFromFav){
            val params = holder.base.layoutParams
            params.height=300
            holder.base.setLayoutParams(params)
        }

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}