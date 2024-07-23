package com.soni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.soni.R

import com.soni.services.web.models.LocationSearchResult
import java.lang.reflect.Field

class AutoCompleteAdapter(
    ctx: Context,
    List: ArrayList<LocationSearchResult>,
    private var onItemClicked: ((pos:Int) -> Unit)):ArrayAdapter<LocationSearchResult>(ctx, 0, List){

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createDropView(position, recycledView, parent)
    }
    private fun createDropView(position: Int, recycledView: View?, parent: ViewGroup): View {

        val item = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.layout_autocomplet_dropdown,
            parent,
            false
        )

        var tv=view.findViewById<TextView>(R.id.text1)
        if (item != null) {
            tv.text=item.LocalizedName!!+", "+item.AdministrativeArea!!.LocalizedName!!

        }
        tv.setOnClickListener {
            onItemClicked(position)
        }

        return view
    }
    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {

        val item = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_dropdown_item_1line,
            parent,
            false
        )
        var tv=view.findViewById<TextView>(android.R.id.text1)
        if (item != null) {
            tv.text=item.LocalizedName!!+", "+item.AdministrativeArea!!.LocalizedName!!
            tv.setTextColor(context.getColor(R.color.Primary100))

        }
        tv.setOnClickListener {
            onItemClicked(position)
        }
        return view
    }
}

