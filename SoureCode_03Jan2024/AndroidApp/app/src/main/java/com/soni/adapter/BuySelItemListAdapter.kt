package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.ProductDetailsActivity
import com.soni.services.web.models.ProductData
import com.soni.utils.Const
import kotlin.collections.ArrayList


class BuySelItemListAdapter(
    val context: Context,
    val isBuy: Boolean,
    var list: ArrayList<ProductData>,
    var OnItemClicked:((pos:Int,isFav:Int)->Unit)
):
    RecyclerView.Adapter<BuySelItemListAdapter.InquiriesHomeAdapterViewHoldes>() {

    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

//        val tv_type = itemView.findViewById<TextView>(R.id.tv_type)
        val base = itemView.findViewById<LinearLayout>(R.id.ll_root)
        val iv_fav = itemView.findViewById<ImageView>(R.id.iv_fav)
        val iv_product_image = itemView.findViewById<ImageView>(R.id.iv_product_image)
        val tv_product_type = itemView.findViewById<TextView>(R.id.tv_product_type)
        val tv_product_name = itemView.findViewById<TextView>(R.id.tv_product_name)
        val tv_product_price = itemView.findViewById<TextView>(R.id.tv_product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_buy_sell_item_list_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        var item=list[position]
        if (isBuy) {
            holder.iv_fav.visibility=View.VISIBLE
        }
        holder.iv_fav.setOnClickListener {
            if (item.favouriteFlag==1)
            {
                item.favouriteFlag=0
                OnItemClicked(position,0)
            }else{
                item.favouriteFlag=1
                OnItemClicked(position,1)
            }

            notifyItemChanged(position)
        }
        if (item.favouriteFlag==1) {
            holder.iv_fav.setImageDrawable(context.getDrawable(R.drawable.selected_star))
        }else{
            holder.iv_fav.setImageDrawable(context.getDrawable(R.drawable.unselected_star))
        }
        if(item.productassets.isNotEmpty()) {
            Glide.with(context)
                .load(item.productassets[0].assetUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.product_home_btn)
                .into(holder.iv_product_image)
        }else{
            Glide.with(context)
                .load(R.drawable.product_home_btn)
                .error(R.drawable.product_home_btn)
                .into(holder.iv_product_image)

        }
        holder.tv_product_name.text=item.title
        holder.tv_product_type.text=item.categoryName
        holder.tv_product_price.text="Rs."+item.price!!.split(".")[0]


        holder.base.setOnClickListener {

            var mIntent= Intent(context, ProductDetailsActivity::class.java)
            mIntent.putExtra(Const.IntentKey.PropertyType, item.categoryId)
            mIntent.putExtra(Const.IntentKey.ProductData,item)
            if(isBuy) {
                mIntent.putExtra(
                    Const.IntentKey.PropertyContactName,item.username
                )
                mIntent.putExtra(Const.IntentKey.PropertyContactNumber, item.phoneNumber)
                mIntent.putExtra(Const.IntentKey.PropertyContactImage, item.profileUrl)
            }else{
                mIntent.putExtra(
                    Const.IntentKey.PropertyContactName,item.username
                )
                mIntent.putExtra(Const.IntentKey.PropertyContactNumber, item.phoneNumber)
                mIntent.putExtra(Const.IntentKey.PropertyContactImage, item.profileUrl)
                mIntent.putExtra(Const.IntentKey.isOwnProperty, true)

            }

            mIntent.putExtra(Const.IntentKey.isOwnProperty,!isBuy)
            context.startActivity(mIntent) }

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}