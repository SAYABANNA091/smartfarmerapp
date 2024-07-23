package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.services.web.models.Propertyassets


class PropertyImagesAdapter(val context: Context, var list: ArrayList<Propertyassets>) :
    PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return if (list.isNotEmpty()) list.size else 1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view =
            layoutInflater!!.inflate(R.layout.layout_porperty_image_adapter, container, false)
        val iv_property_img = view.findViewById<ImageView>(R.id.iv_property_img)
        if (list.isNotEmpty()) {
            Glide.with(context)
                .load(list[position].assetUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.rafiki)
                .into(iv_property_img)
        } else {
            Glide.with(context)
                .load(R.drawable.rafiki)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(iv_property_img)

        }
        container.addView(view, position)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
//        val iv_property_img = view.findViewById<ImageView>(R.id.iv_property_img)
//        iv_property_img.setImageDrawable(context.resources.getDrawable(images[position]))
//        return true
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        val view = `object` as View
//        container.removeView(view)
    }

}