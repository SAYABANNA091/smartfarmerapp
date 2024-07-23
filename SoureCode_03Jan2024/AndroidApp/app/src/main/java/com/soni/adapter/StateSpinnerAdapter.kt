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
import com.soni.services.web.models.State
import java.lang.reflect.Field

class StateSpinnerAdapter(
    ctx: Context,
    List: ArrayList<State>,):ArrayAdapter<State>(ctx, 0, List){

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
            tv.text=item.name
        }

        return view
    }
    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {

        val item = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.layout_autocomplet_dropdown,
            parent,
            false
        )

        var tv=view.findViewById<TextView>(R.id.text1)
        if (item != null) {
            tv.text=item.name

        }

        return view
    }
}

