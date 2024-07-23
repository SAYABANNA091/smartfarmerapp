package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class InquiryListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("data"        ) var data        : ArrayList<PropertyModel> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)




data class InquiryData (

    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("property_id"   ) var propertyId   : Int?    = null,
    @SerializedName("name"          ) var name         : String? = null,
    @SerializedName("property_name" ) var propertyName : String? = null

)