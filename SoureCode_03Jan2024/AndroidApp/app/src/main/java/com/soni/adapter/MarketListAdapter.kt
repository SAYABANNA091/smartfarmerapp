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
import com.soni.activity.MarketDetailsActivity
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.InquiryData
import com.soni.services.web.models.MarketData
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue


class MarketListAdapter(val context: Context, var list:ArrayList<MarketData>):
    RecyclerView.Adapter<MarketListAdapter.InquiriesHomeAdapterViewHoldes>() {
    val formatin = SimpleDateFormat("dd/MM/yyyy")
    val formatout = SimpleDateFormat("dd MMMM yyyy")
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){


        val llBase = itemView.findViewById<LinearLayout>(R.id.ll_base)
        val tv_commodity_name = itemView.findViewById<TextView>(R.id.tv_commodity_name)
        val tv_commodity_market = itemView.findViewById<TextView>(R.id.tv_commodity_market)
        val tv_commodity_price = itemView.findViewById<TextView>(R.id.tv_commodity_price)
        val tv_last_update = itemView.findViewById<TextView>(R.id.tv_last_update)
        val tv_price_difference = itemView.findViewById<TextView>(R.id.tv_price_difference)
        val iv_trend_icon = itemView.findViewById<ImageView>(R.id.iv_trend_icon)
        val iv_price_difference_icon = itemView.findViewById<ImageView>(R.id.iv_price_difference_icon)
        val ll_price_difference = itemView.findViewById<LinearLayout>(R.id.ll_price_difference)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_market_list_item_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {
        var item=list[position]


        holder.tv_commodity_name.text=if (item.commodity!=null) item.commodity else ""
        holder.tv_commodity_market.text=if (item.market!=null) item.market else ""
        holder.tv_commodity_price.text=if (item.modalPrice!=null) "₹ ${item.modalPrice}/Q" else ""
        if(item.arrivalDate!=null){
            var outstr=formatout.format(formatin.parse(item.arrivalDate!!))
            holder.tv_last_update.text=outstr
        }else{
            holder.tv_last_update.text=""
        }
        if (item.compare_rate!=null) {
            if(item.compare_rate==0){
                holder.iv_trend_icon.visibility=View.INVISIBLE
                holder.iv_price_difference_icon.visibility=View.GONE
                holder.ll_price_difference.background =context.getDrawable(R.drawable.yellow_btn_rounded)
                holder.tv_price_difference.text = "-  ₹0"
            }
          else  {
                if (item.compare_rate!! < 0) {
                    holder.iv_trend_icon.setImageDrawable(context.getDrawable(R.drawable.market_down))
                    holder.iv_price_difference_icon.setImageDrawable(context.getDrawable(R.drawable.arrow_down_white))
                    holder.ll_price_difference.background =
                        context.getDrawable(R.drawable.red_btn_rounded)

                }
                holder.tv_price_difference.text = "₹" + item.compare_rate!!.absoluteValue.toString()
            }
        }
        else{
            holder.iv_trend_icon.visibility=View.INVISIBLE
            holder.iv_price_difference_icon.visibility=View.INVISIBLE
            holder.ll_price_difference.background =context.getDrawable(R.drawable.transparent_5sdp_radius)
            holder.tv_price_difference.text = ""
        }
        holder.tv_commodity_name.text=if (item.commodity!=null) item.commodity else ""
//        Glide.with(context)
//            .load(item.profileUrl)
//            .placeholder(R.drawable.logo)
//            .error(R.drawable.logo)
//            .into(holder.iv_employee_pic)
        holder.setIsRecyclable(false);

        holder.llBase.setOnClickListener {
            var intent=Intent(context,MarketDetailsActivity::class.java)
            intent.putExtra(Const.IntentKey.State,item.state)
            intent.putExtra(Const.IntentKey.Commodity,item.commodity)
            intent.putExtra(Const.IntentKey.Varity,item.variety)
            intent.putExtra(Const.IntentKey.Market,item.market)
            intent.putExtra(Const.IntentKey.Difference,item.compare_rate)

            context.startActivity(intent)
        }
        holder.setIsRecyclable(false)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}