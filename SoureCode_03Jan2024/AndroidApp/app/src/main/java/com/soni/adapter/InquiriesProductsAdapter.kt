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
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.BaseActivity
import com.soni.activity.ProductDetailsActivity
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.ProductData
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InquiriesProductsAdapter(val context: Context, var list: ArrayList<ProductData>):
    RecyclerView.Adapter<InquiriesProductsAdapter.InquiriesHomeAdapterViewHoldes>() {
    class InquiriesHomeAdapterViewHoldes(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtNotification = itemView.findViewById<TextView>(R.id.txt_name_tacket)
        val tv_close = itemView.findViewById<TextView>(R.id.tv_close)
        val tv_complete = itemView.findViewById<TextView>(R.id.tv_complete)
        val iv_employee_pic = itemView.findViewById<ImageView>(R.id.iv_employee_pic)

        val llBase = itemView.findViewById<LinearLayout>(R.id.ll_base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiriesHomeAdapterViewHoldes {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_inquiries_products_adapter,parent,false)
        return InquiriesHomeAdapterViewHoldes(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: InquiriesHomeAdapterViewHoldes, position: Int) {

        var item=list[position]
        var text:String=""
        
            text=  buildString {
                append(context.getString(R.string.inquirytext))
                append(" ")
                append(item.username)
                append(" ")
                append(context.getString(R.string.about))
                append(" ")
                append(item.title)
                append(" ")
                append(context.getString(R.string.and_qty))
                append(" ")
                append(item.qty)
                append(".")
            }
        

        val wordtoSpan: Spannable =
            SpannableString(text)
        try {
            wordtoSpan.setSpan(
                ForegroundColorSpan(context.getColor(R.color.Primary100)),text.split(context.getString(R.string.about))[0].length+5,
                text.split(context.getString(R.string.about))[0].length+ 5 +  text.split(context.getString(R.string.about))[1].split(context.getString(R.string.and))[0].length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }catch (e:Exception){

        }
        try {
            wordtoSpan.setSpan(
                StyleSpan(Typeface.BOLD)
                ,text.split(context.getString(R.string.about))[0].length+5,
                text.split(context.getString(R.string.about))[0].length+ 5 +  text.split(context.getString(R.string.about))[1].split(context.getString(
                    R.string.and))[0].length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }catch (e:Exception){

        }
        try {
            wordtoSpan.setSpan(
                ForegroundColorSpan(context.getColor(R.color.Primary100)),text.split(context.getString(R.string.`is`))[0].length+2,
                text.split(context.getString(R.string.`is`))[0].length+ 2 +  text.split(context.getString(R.string.`is`))[1].length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }catch (e:Exception){

        }





        holder.txtNotification.text = wordtoSpan
        holder.llBase.setOnClickListener {

            var mIntent= Intent(context, ProductDetailsActivity::class.java)
            mIntent.putExtra(Const.IntentKey.ProductID, (item.id))
            mIntent.putExtra(Const.IntentKey.ProductData,item)
            mIntent.putExtra(Const.IntentKey.PropertyContactName, item.username)
            mIntent.putExtra(Const.IntentKey.PropertyContactNumber, item.phoneNumber)
            mIntent.putExtra(Const.IntentKey.PropertyContactImage, item.profileUrl)

            mIntent.putExtra(Const.IntentKey.isOwnProperty,true)
            mIntent.putExtra(Const.IntentKey.isFromInquiry,true)
            context.startActivity(mIntent)
        }
        if (item.inquiry_status=="0") {
            holder.tv_close.setOnClickListener {
                callUpdateProductInquiry(item.inquiryId!!.toString(), "1")
                list[position].inquiry_status="1"
                notifyDataSetChanged()
            }
            holder.tv_complete.setOnClickListener {
                callUpdateProductInquiry(item.inquiryId!!.toString(), "2")
                list[position].inquiry_status="2"
                notifyDataSetChanged()

            }
        }else{
            if (item.inquiry_status=="1"){
                holder.tv_complete.visibility=View.GONE
                holder.tv_close.background= AppCompatResources.getDrawable(context,R.drawable.btn_bg_greay100)
            }
            if (item.inquiry_status=="2"){
                holder.tv_close.visibility=View.GONE
                holder.tv_complete.background=AppCompatResources.getDrawable(context,R.drawable.btn_bg_greay100)
            }
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

//    postUpdateProductInquiry

//    inquiry_id:1
//    inquiry_status:2 1- close 2- complete
    private fun callUpdateProductInquiry(id:String,status:String) {
        if (( context as BaseActivity) .isInternetAvailable(context)) {

            var param: HashMap<String, String> = HashMap()
            param["inquiry_id"] = id
            param["inquiry_status"] = status

            SoniApp.mApiCall?.postUpdateProductInquiry(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        ( context as BaseActivity).showToast(context.getString(R.string.api_error))
//                        binding.rvRentItems.visibility = View.VISIBLE
//                        binding.sfShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                               try {
                                   ( context as BaseActivity).showToast(responseLogin.message!!)
                                   notifyDataSetChanged()
                               }catch (e:Exception){
                                   ( context as BaseActivity).showToast(context.getString(R.string.api_error))

                               }
                            }
                        }
                    }


                })

        } else {
            ( context as BaseActivity).showToast(( context as BaseActivity).getString(R.string.internet_error))

        }
    }
}