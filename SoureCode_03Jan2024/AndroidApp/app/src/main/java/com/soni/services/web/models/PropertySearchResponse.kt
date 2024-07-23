package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class PropertySearchResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("data"        ) var data        : ArrayList<PropertyModel> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)