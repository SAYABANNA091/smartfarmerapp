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


class MessingItemListAdapter(val context: Context, var details: ArrayList<String>) :
    RecyclerView.Adapter<MessingItemListAdapter.InquiriesHomeAdapterViewHoldes>() {

    class InquiriesHomeAdapterViewHoldes(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.tv_missing_item)
       }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_missing_details, parent, false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        var item = details[position]
        holder.name.text=item
        holder.setIsRecyclable(false);
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}