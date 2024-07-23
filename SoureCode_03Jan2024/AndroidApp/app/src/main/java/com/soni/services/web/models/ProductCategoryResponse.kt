package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class ProductCategoryResponse  (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("data"        ) var data        : ArrayList<ProductType> = arrayListOf(),
    @SerializedName("message"     ) var message     : String?         = null,

)

