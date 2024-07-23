package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class BaseModel (

    @SerializedName("status"  ) var status  : Boolean?    = null,
    @SerializedName("message" ) var message : String? = null

)