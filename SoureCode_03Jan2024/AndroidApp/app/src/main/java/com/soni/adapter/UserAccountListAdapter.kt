package com.soni.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.SwipeListener
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.HomeActivity
import com.soni.activity.PropertyDetailActivity
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const


class UserAccountListAdapter(val context: Context, var list: ArrayList<PropertyModel>, private var onItemClicked: ((item:Int) -> Unit )) :
    RecyclerView.Adapter<UserAccountListAdapter.UserAccountListAdapterViewHolder>() {
    private var viewBinderHelper = ViewBinderHelper()
    class UserAccountListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iv_property_img = itemView.findViewById<ImageView>(R.id.iv_property_img)
        val txt_price = itemView.findViewById<TextView>(R.id.txt_price)
        val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        val tv_tractor_name = itemView.findViewById<TextView>(R.id.txt_tractor_name)
        val tv_village_name = itemView.findViewById<TextView>(R.id.txt_village_name)
        val txt_price_per = itemView.findViewById<TextView>(R.id.txt_price_per)
        val tv_hp = itemView.findViewById<TextView>(R.id.tv_hp)
        val llRoot = itemView.findViewById<LinearLayout>(R.id.ll_root)
        val edit_layout = itemView.findViewById<LinearLayout>(R.id.edit_layout)
        val delete_layout = itemView.findViewById<LinearLayout>(R.id.delete_layout)
        val swipe_layout = itemView.findViewById<SwipeRevealLayout>(R.id.swipe_layout)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAccountListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user_account_list_adapter, parent, false)
        viewBinderHelper.setOpenOnlyOne(true)
        return UserAccountListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
//        return 50
    }
    fun saveStates(outState: Bundle?) {
        viewBinderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        viewBinderHelper.restoreStates(inState)
    }
    public fun onDelete(pos:Int){
        list.removeAt(pos)
        viewBinderHelper=ViewBinderHelper()
        notifyDataSetChanged()


    }
    public fun onUpdate(){
        viewBinderHelper=ViewBinderHelper()
        notifyDataSetChanged()


    }

    override fun onBindViewHolder(holder: UserAccountListAdapterViewHolder, position: Int) {
        var dataObject: PropertyModel = list[position]


        if(holder.swipe_layout!=null && viewBinderHelper!=null) {
            viewBinderHelper.bind(holder.swipe_layout, position.toString());
        }
        holder.edit_layout.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("screenType", 1)
            bundle.putInt(Const.IntentKey.PropertyType, (dataObject.propertyTypeId!!))
            bundle.putSerializable(Const.IntentKey.Property,dataObject)
            (context as HomeActivity).seEditPropertiesFragment(bundle, Const.TAB.home)

        }
        holder.delete_layout.setOnClickListener {
            onItemClicked(position)
        }
        holder.llRoot.setOnClickListener {
            var mIntent = Intent(context, PropertyDetailActivity::class.java)
            mIntent.putExtra(Const.IntentKey.PropertyType, (dataObject.propertyTypeId!!))
            mIntent.putExtra(Const.IntentKey.PropertyContactName, "${if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else ""} ${if (SoniApp.user!!.lastName != null) SoniApp.user!!.lastName else ""} ")
            mIntent.putExtra(Const.IntentKey.PropertyContactNumber, "${SoniApp.user!!.phoneNumber}")
            mIntent.putExtra(Const.IntentKey.PropertyContactImage, "${SoniApp.user!!.profileUrl}")
            mIntent.putExtra(Const.IntentKey.isOwnProperty, true)
            mIntent.putExtra(Const.IntentKey.Property,dataObject)

            context.startActivity(mIntent)
        }
        if (dataObject.propertyassets.isNotEmpty()) {
            Glide.with(context as HomeActivity)
                .load(dataObject.propertyassets[0].assetUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.iv_property_img)
        }
        var type=""
       when(dataObject.propertyTypeId){
           1->type="Tractor"
           2->type="JCB"
           3->type="Farm"
           4->type="Nursery"
           5->type="Vermicompost"
       }
        holder.tv_title.text = type
        when (dataObject.propertyTypeId!!) {
            1 -> {


                holder.tv_tractor_name.text = dataObject.tractorTitle
                if(dataObject.tractorVillageName!! == "-" && dataObject.tractorPincode!! == "-"){
                    holder.tv_village_name.text = context.getString(R.string.location_not_available)
                }else{
                    holder.tv_village_name.text = dataObject.tractorVillageName +", "+if (dataObject.tractorPincode.toString()=="null") "" else dataObject.tractorPincode
                }

                holder.tv_hp.text =  "${if(dataObject!!.tractorHoursePower.toString()== "0")"-" else dataObject!!.tractorHoursePower.toString()+" HP"}"
                holder.txt_price.text =if(dataObject!!.tractorPricePerHour.toString().split(".")[0] == "0" )"NA" else "Rs.${dataObject!!.tractorPricePerHour.toString().split(".")[0]}"

            }
            2 -> {
                holder.tv_tractor_name.text = dataObject.jcbTitle
                if(dataObject.jcbVillageName!! == "-" && dataObject.jcbPincode!! == "-"){
                    holder.tv_village_name.text = context.getString(R.string.location_not_available)
                }else{
                    holder.tv_village_name.text = dataObject.jcbVillageName+", "+if (dataObject.jcbPincode.toString()=="null") "" else dataObject.jcbPincode
                }
                holder.tv_hp.text = "${if(dataObject!!.jcbHoursePower.toString()== "0")"-" else dataObject!!.jcbHoursePower.toString()+" HP"} "
                holder.txt_price.text =if(dataObject!!.jcbPricePerHour.toString().split(".")[0] == "0" )"-" else "Rs.${dataObject!!.jcbPricePerHour.toString().split(".")[0]}"

            }
            3 -> {
                holder.tv_tractor_name.text = dataObject.farmTitle
                if(dataObject.farmVillegeName!! == "-" && dataObject.farmPincode!! == "-"){
                    holder.tv_village_name.text = context.getString(R.string.location_not_available)
                }else{
                    holder.tv_village_name.text = dataObject.farmVillegeName+", "+dataObject.farmPincode
                }

                holder.tv_hp.text = "${dataObject.farmNoOfAcers} Acres"
                holder.txt_price.visibility=View.INVISIBLE
                holder.txt_price_per.visibility=View.INVISIBLE
            }
            4 -> {
                holder.tv_tractor_name.text = dataObject.nurseryTitle
                if(dataObject.nurseryVillageName!! == "-" && dataObject.nurseryPincode!! == "-"){
                    holder.tv_village_name.text = context.getString(R.string.location_not_available)
                }else{
                    holder.tv_village_name.text = dataObject.nurseryVillageName+", "+dataObject.nurseryPincode
                }
                holder.tv_hp.visibility=View.INVISIBLE
                holder.txt_price.visibility=View.INVISIBLE
                holder.txt_price_per.visibility=View.INVISIBLE
            }
            5 -> {
                holder.tv_tractor_name.text = dataObject.vermicompostTitle
                if(dataObject.vermicompostVillageName!! == "-" && dataObject.vermicompostPincode!! == "-"){
                    holder.tv_village_name.text = context.getString(R.string.location_not_available)
                }else{
                    holder.tv_village_name.text = dataObject.vermicompostVillageName+", "+dataObject.vermicompostPincode
                }
                holder.tv_hp.visibility=View.INVISIBLE
                holder.txt_price.visibility=View.INVISIBLE
                holder.txt_price_per.visibility=View.INVISIBLE
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