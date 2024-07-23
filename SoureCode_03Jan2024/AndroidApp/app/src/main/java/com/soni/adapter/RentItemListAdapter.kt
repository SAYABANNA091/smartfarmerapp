package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.soni.activity.BaseActivity
import com.soni.activity.HomeActivity
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RentItemListAdapter(val context: Context, var propertyList: ArrayList<PropertyModel>) :
    RecyclerView.Adapter<RentItemListAdapter.InquiriesHomeAdapterViewHoldes>() {

    class InquiriesHomeAdapterViewHoldes(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val base = itemView.findViewById<LinearLayout>(R.id.ll_root)
        val name = itemView.findViewById<TextView>(R.id.tv_property_name)
        val address = itemView.findViewById<TextView>(R.id.tv_address)
        val hp = itemView.findViewById<TextView>(R.id.tv_hp)
        val layOutPrice = itemView.findViewById<LinearLayout>(R.id.ll_price)
        val price = itemView.findViewById<TextView>(R.id.tv_price)
        val image = itemView.findViewById<ImageView>(R.id.iv_property_image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rent_requent_item_list_adapter, parent, false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        var item = propertyList[position]
        when (item.propertyTypeId) {
            1 -> {
                holder.name.text = item.tractorTitle
                if(item.tractorVillageName!! =="-" &&item.tractorPincode!! =="-"){
                    holder.address.text = context.getString(R.string.location_not_available)

                }else{
                    holder.address.text = "${item.tractorVillageName}, ${if(item.tractorPincode.toString()=="null") "" else item.tractorPincode}"
                }
                holder.hp.text = "${if(item!!.tractorHoursePower.toString()== "0")"-" else item!!.tractorHoursePower.toString()+" HP"}"
                holder.price.text =if(item!!.tractorPricePerHour.toString().split(".")[0] == "0" )"NA" else "Rs.${item!!.tractorPricePerHour.toString().split(".")[0]}"
            }
            2 -> {
                holder.name.text = item.jcbTitle
                if(item.jcbVillageName!! =="-" &&item.jcbPincode!! =="-"){
                    holder.address.text = context.getString(R.string.location_not_available)

                }else{
                    holder.address.text = "${item.jcbVillageName},  ${if(item.jcbPincode.toString()=="null") "" else item.jcbPincode}"
                }
                holder.hp.text = "${if(item!!.jcbHoursePower.toString()== "0")"-" else item!!.jcbHoursePower.toString()+"HP"} "
                holder.price.text =if(item!!.jcbPricePerHour.toString().split(".")[0] == "0" )"-" else "Rs.${item!!.jcbPricePerHour.toString().split(".")[0]}"
            }
            3 -> {
                holder.name.text = item.farmTitle
                if(item.farmVillegeName!! =="-" &&item.farmPincode!! =="-"){
                    holder.address.text = context.getString(R.string.location_not_available)

                }else{
                    holder.address.text = "${item.farmVillegeName},${item.farmPincode}"
                }
                holder.hp.text = "${item.farmNoOfAcers.toString()} Acre"
                holder.layOutPrice.visibility = View.INVISIBLE
            }
            4 -> {
                holder.name.text = item.nurseryTitle
                if(item.nurseryVillageName!! =="-" &&item.nurseryPincode!! =="-"){
                    holder.address.text = context.getString(R.string.location_not_available)

                }else{
                    holder.address.text = "${item.nurseryVillageName},${item.nurseryPincode}"
                }
                holder.hp.text = ""
                holder.layOutPrice.visibility = View.INVISIBLE
            }
            5 -> {
                holder.name.text = item.vermicompostTitle
                if(item.vermicompostVillageName!! =="-" &&item.vermicompostPincode!! =="-"){
                    holder.address.text = context.getString(R.string.location_not_available)

                }else{
                    holder.address.text = "${item.vermicompostVillageName},${item.vermicompostPincode}"
                }
                holder.hp.text = ""
                holder.layOutPrice.visibility = View.INVISIBLE
            }
        }
        if (!item.propertyassets.isNullOrEmpty()) {
            if (item.propertyassets[0].assetUrl != null) {
                if (item.propertyassets[0].assetUrl.toString().isNotEmpty()) {
                    Glide.with(context as BaseActivity)
                        .load(item.propertyassets[0].assetUrl!!)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.image)
                }
            }
        }else{
            Glide.with(context as BaseActivity)
                .load(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.image)
        }
        holder.base.setOnClickListener {
            try {
                var mIntent = Intent(context, PropertyDetailActivity::class.java)
                mIntent.putExtra(Const.IntentKey.PropertyType, (item.propertyTypeId))
                mIntent.putExtra(Const.IntentKey.PropertyContactName, item.userdetails!!.firstName +" "+item.userdetails!!.lastName)
                mIntent.putExtra(Const.IntentKey.PropertyContactNumber, item.userdetails!!.phoneNumber)
                mIntent.putExtra(Const.IntentKey.PropertyContactImage, item.userdetails!!.profileUrl)
                mIntent.putExtra(Const.IntentKey.Property,item)
                mIntent.putExtra(Const.IntentKey.isOwnProperty, false)
                context.startActivity(mIntent)
            }catch (e:java.lang.Exception){
            Log.d("CATCH",e.message.toString())
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