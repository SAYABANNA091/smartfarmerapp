package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import java.text.SimpleDateFormat
import java.util.*


class AttendanceDateAdapter(
    val context: Context,
    private var onItemClicked: ((item: Date) -> Unit)
) :
    RecyclerView.Adapter<AttendanceDateAdapter.InquiriesHomeAdapterViewHoldes>() {
    val formatterDay = SimpleDateFormat("dd")
    val formatterDate = SimpleDateFormat("EEE")
    var selectedPos = 0
    var days = Calendar.getInstance().time.date
    var current = Calendar.getInstance()

    fun onMonthChange(time: Calendar, isCurrent: Boolean) {
        selectedPos = -1
        current = time

        days = if (isCurrent) {
            Calendar.getInstance().time.date
        } else {
            getDaysOfMonth(current)
        }

        if (isCurrent) {
            current.add(Calendar.DATE, days)
        }
        notifyDataSetChanged()

    }

    class InquiriesHomeAdapterViewHoldes(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_day = itemView.findViewById<TextView>(R.id.tv_day)
        val tv_date = itemView.findViewById<TextView>(R.id.tv_date)
        val base = itemView.findViewById<LinearLayout>(R.id.base)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_attendance_date_adapter, parent, false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return days
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
            if (position==0){
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                var vertical=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp).toInt()
                var right=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp).toInt()
                var left=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toInt()
                params.width=context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._50sdp).toInt()
                params.setMargins(left, vertical, right, vertical)
                holder.base.setLayoutParams(params)
        }
        var now = current.clone() as Calendar
        now.add(Calendar.DATE, -position)


        holder.tv_day.text = formatterDay.format(now.time)
        holder.tv_date.text = formatterDate.format(now.time)
        if (position != selectedPos) {
            holder.base.background = context.getDrawable(R.drawable.account_5sdp_radius)
            holder.tv_day.setTextColor(context.getColor(R.color.Primary100))
            holder.tv_date.setTextColor(context.getColor(R.color.Primary100))

        } else {
            holder.base.background = context.getDrawable(R.drawable.btn_bg_primery)
            holder.tv_day.setTextColor(context.getColor(R.color.White100))
            holder.tv_date.setTextColor(context.getColor(R.color.White100))

        }
        holder.base.setOnClickListener {
            selectedPos = position
            onItemClicked(now.time)
            notifyDataSetChanged()
        }

    }

    fun getDaysOfMonth(now: Calendar): Int {

        var year = now.get(Calendar.YEAR)
        var month = now.get(Calendar.MONTH) + 1

        var leap = false
        leap = if (year % 4 == 0) {
            if (year % 100 == 0) {
                year % 400 == 0
            } else {
                true
            }
        } else {
            false
        }


        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> {
                return 31
            }
            4, 6, 9, 11 -> {
                return 30
            }
            2 -> {
                return if (leap) 29 else 28
            }
        }
        return 0
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}