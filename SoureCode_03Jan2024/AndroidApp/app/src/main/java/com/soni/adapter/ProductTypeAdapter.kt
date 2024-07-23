package com.soni.adapter

import android.R.attr.left
import android.R.attr.right
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.SoniApp
import java.util.*


class ProductTypeAdapter(val context: Context, private var onItemClicked: ((pos:Int) -> Unit)):
    RecyclerView.Adapter<ProductTypeAdapter.InquiriesHomeAdapterViewHoldes>() {

    var seletedPos=0

    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val tv_type = itemView.findViewById<TextView>(R.id.tv_type)
        val base = itemView.findViewById<LinearLayout>(R.id.base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rent_item_type_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  SoniApp.productType.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        if (position==0){
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            var vertical=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp).toInt()
            var left=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp).toInt()
            var right=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toInt()
            params.setMargins(left, vertical, right, vertical)
            holder.base.setLayoutParams(params)
        }else if (position==SoniApp.productType.size-1){
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            var vertical=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp).toInt()
            var right=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp).toInt()
            var left=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toInt()
            params.setMargins(left, vertical, right, vertical)
            holder.base.setLayoutParams(params)
        }
        holder.tv_type.text =SoniApp.productType[position].name
        if (position!=seletedPos){
            holder.base.background=context.getDrawable(R.drawable.account_5sdp_radius)
            holder.tv_type.setTextColor(context.getColor(R.color.Primary100))

        }else{
            holder.base.background=context.getDrawable(R.drawable.btn_bg_primery)
            holder.tv_type.setTextColor(context.getColor(R.color.White100))
        }
        holder.base.setOnClickListener {
            if(seletedPos!=position) {
                seletedPos = position
                onItemClicked(if(SoniApp.productType[position].id!=null) SoniApp.productType[position].id!! else position)
                notifyDataSetChanged()
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