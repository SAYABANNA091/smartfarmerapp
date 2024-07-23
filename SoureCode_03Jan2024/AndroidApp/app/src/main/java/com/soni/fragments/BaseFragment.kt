package com.soni.fragments

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment() {
    lateinit var ivBack:ImageView
    lateinit var ivRight:ImageView
    lateinit var tvTitle:TextView


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Date.ddMmYyyy(): String {
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(this)
    }
    fun Date.yyyyMMDD(): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(this)
    }

}