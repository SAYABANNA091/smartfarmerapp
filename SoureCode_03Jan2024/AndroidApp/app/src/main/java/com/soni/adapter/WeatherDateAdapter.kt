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


class WeatherDateAdapter(
    val context: Context,
    var list :ArrayList<HourlyForecast>,
    private var onItemClicked: ((pos:Int) -> Unit)
) :
    RecyclerView.Adapter<WeatherDateAdapter.InquiriesHomeAdapterViewHoldes>() {
    val formatterDay = SimpleDateFormat("hh aa")
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZ")
    var selectedPos = 0


    class InquiriesHomeAdapterViewHoldes(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val base = itemView.findViewById<LinearLayout>(R.id.base)
        val tv_time = itemView.findViewById<TextView>(R.id.tv_time)
        val tv_temp = itemView.findViewById<TextView>(R.id.tv_temp)
        val tv_c = itemView.findViewById<TextView>(R.id.tv_c)
        val iv_weather_icon = itemView.findViewById<ImageView>(R.id.iv_weather_icon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_weather_date_adapter, parent, false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return list.size
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
//        var now = current.clone() as Calendar
//        now.add(Calendar.DATE, -position)

        val gmt = formatter.parse(list[position].DateTime)

        holder.tv_time.text = formatterDay.format(gmt)
        if(list[position].WeatherIcon.toString() !="null") {
            holder.iv_weather_icon.setImageResource((getWeatherIcon(list[position].WeatherIcon!!.toInt())))
        }
        holder.tv_temp.text = list[position].Temperature!!.Value!!.toString()
        if (position != selectedPos) {
            holder.base.background = context.getDrawable(R.drawable.account_5sdp_radius)
            holder.tv_time.setTextColor(context.getColor(R.color.Primary100))
            holder.tv_temp.setTextColor(context.getColor(R.color.Primary100))
            holder.tv_c.setTextColor(context.getColor(R.color.Primary100))

        } else {
            holder.base.background = context.getDrawable(R.drawable.btn_bg_primery)
            holder.tv_time.setTextColor(context.getColor(R.color.White100))
            holder.tv_temp.setTextColor(context.getColor(R.color.White100))
            holder.tv_c.setTextColor(context.getColor(R.color.White100))
        }
        holder.base.setOnClickListener {
            selectedPos = position
            notifyDataSetChanged()
            onItemClicked(position)
           }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



}