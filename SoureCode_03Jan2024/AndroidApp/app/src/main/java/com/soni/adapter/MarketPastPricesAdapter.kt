package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soni.R
import com.soni.services.web.models.MarketData
import java.text.SimpleDateFormat


class MarketPastPricesAdapter(val context: Context, var list: ArrayList<MarketData>):
    RecyclerView.Adapter<MarketPastPricesAdapter.InquiriesHomeAdapterViewHoldes>() {
    val formatin = SimpleDateFormat("dd/MM/yyyy")
    val formatout = SimpleDateFormat("dd MMMM yyyy")
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){


        val llBase = itemView.findViewById<LinearLayout>(R.id.ll_base)
        val tv_day = itemView.findViewById<TextView>(R.id.tv_day)
        val tv_price = itemView.findViewById<TextView>(R.id.tv_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_market_past_prices_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        var item=list[position]
        var outstr=formatout.format(formatin.parse(item.arrivalDate!!))
        holder.tv_day.text=outstr

        holder.tv_price.text=if (item.modalPrice!=null) "₹ ${item.modalPrice}/Q" else ""

        holder.setIsRecyclable(false);

    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}