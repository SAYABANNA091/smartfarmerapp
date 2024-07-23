package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.SoniApp
import com.soni.services.web.models.DailyForecasts
import java.text.SimpleDateFormat


class WeeksForecastAdapter(val context: Context,var list: ArrayList<DailyForecasts>, ):
    RecyclerView.Adapter<WeeksForecastAdapter.InquiriesHomeAdapterViewHoldes>() {
    val formatterDay = SimpleDateFormat("EEEE")
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZ")

    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){


        val tv_day = itemView.findViewById<TextView>(R.id.tv_day)
        val tv_temp_max = itemView.findViewById<TextView>(R.id.tv_temp_max)
        val tv_temp_min = itemView.findViewById<TextView>(R.id.tv_temp_min)
        val iv_weather_icon = itemView.findViewById<ImageView>(R.id.iv_weather_icon)

        val ll_base = itemView.findViewById<LinearLayout>(R.id.ll_base)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_weeks_forcast_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {

        holder.ll_base.setOnClickListener {
        }
        holder.tv_temp_max.text=list[position].Temperature!!.Maximum!!.Value.toString()
        holder.tv_temp_min.text=list[position].Temperature!!.Minimum!!.Value.toString()
        val gmt = formatter.parse(list[position].Date!!)

        holder.tv_day.text = formatterDay.format(gmt)
        if(list[position].Day!!.Icon.toString() !="null") {
            holder.iv_weather_icon.setImageResource((SoniApp.getWeatherIcon(list[position].Day!!.Icon!!.toInt())))
        }

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



}