package com.soni.utils

import android.location.Location

interface LocationCallbackListener {
    fun onUpdate(location: Location)
}

interface MapCallBack {
    fun onUpdate(lat: Number,long : Number)
}