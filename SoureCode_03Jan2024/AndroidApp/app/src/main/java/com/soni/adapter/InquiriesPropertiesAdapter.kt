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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.InquiryData
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const


class InquiriesPropertiesAdapter(val context: Context, var list:ArrayList<PropertyModel>):
    RecyclerView.Adapter<InquiriesPropertiesAdapter.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val iv_employee_pic = itemView.findViewById<ImageView>(R.id.iv_employee_pic)
        val llBase = itemView.findViewById<LinearLayout>(R.id.ll_base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_inquiries_home_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {

        var item=list[position]
        var text:String=""
        when(item.propertyTypeId){
            1-> {
                text=  buildString {
                    append(context.getString(R.string.inquirytext))
                    append(item.name)
                    append(context.getString(R.string.about))
                    append(item.tractorTitle)
                    append(".")
                }
            }
            2->{
                text=  buildString {
                    append(context.getString(R.string.inquirytext))
                    append(item.name)
                    append(context.getString(R.string.about))
                    append(item.jcbTitle)
                    append(".")
                }
            }
            3->{
                text=  buildString {
                    append(context.getString(R.string.inquirytext))
                    append(item.name)
                    append(context.getString(R.string.about))
                    append(item.farmTitle)
                    append(".")
                }
            }
            4->{
                text=  buildString {
                    append(context.getString(R.string.inquirytext))
                    append(item.name)
                    append(context.getString(R.string.about))
                    append(item.nurseryTitle)
                    append(".")
                }
            }
            5->{
                text=  buildString {
                    append(context.getString(R.string.inquirytext))
                    append(item.name)
                    append(context.getString(R.string.about))
                    append(item.vermicompostTitle)
                    append(".")
                }
            }
        }
        val wordtoSpan: Spannable =
            SpannableString(text)

        wordtoSpan.setSpan(
            ForegroundColorSpan(context.getColor(R.color.Primary100)),text.split("about")[0].length+5,
            text.split("about")[0].length+5+  text.split("about")[1].length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(
            StyleSpan(Typeface.BOLD)
            ,text.split("about")[0].length+5,
            text.split("about")[0].length+5+  text.split("about")[1].length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.txtNotification.text = wordtoSpan
        holder.llBase.setOnClickListener {

            var mIntent= Intent(context, PropertyDetailActivity::class.java)
            mIntent.putExtra(Const.IntentKey.PropertyType, (item.propertyTypeId))
            mIntent.putExtra(Const.IntentKey.Property,item)
            mIntent.putExtra(Const.IntentKey.PropertyContactName, item.name)
            mIntent.putExtra(Const.IntentKey.PropertyContactNumber, item.phoneNumber)
            mIntent.putExtra(Const.IntentKey.PropertyContactImage, item.profileUrl)

            mIntent.putExtra(Const.IntentKey.isOwnProperty,true)
            context.startActivity(mIntent)
        }

        Glide.with(context)
            .load(item.profileUrl)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.iv_employee_pic)
        holder.setIsRecyclable(false);
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}