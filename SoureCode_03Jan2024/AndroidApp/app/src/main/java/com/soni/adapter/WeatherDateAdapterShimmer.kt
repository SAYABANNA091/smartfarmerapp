package com.soni.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.SoniApp.Companion.getWeatherIcon
import com.soni.services.web.models.HourlyForecast
import java.text.SimpleDateFormat
import java.util.*


class WeatherDateAdapterShimmer(
    val context: Context,
) :
    RecyclerView.Adapter<WeatherDateAdapterShimmer.InquiriesHomeAdapterViewHoldes>() {


    class InquiriesHomeAdapterViewHoldes(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val base = itemView.findViewById<LinearLayout>(R.id.base)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_weather_date_shimmer_adapter, parent, false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
            if (position==0){
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                var vertical=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp).toInt()
                var right=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp).toInt()
                var left=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp).toInt()
                params.width=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._50sdp).toInt()
                params.setMargins(left, vertical, right, vertical)
                holder.base.setLayoutParams(params)
        }else if (position==getItemCount()-1){
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                var vertical=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp).toInt()
                var left=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp).toInt()
                var right=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp).toInt()
                params.width=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._50sdp).toInt()
                params.setMargins(left, vertical, right, vertical)
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