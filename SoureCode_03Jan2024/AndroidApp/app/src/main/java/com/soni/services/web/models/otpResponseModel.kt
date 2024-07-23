package com.soni.services.web.models
import com.google.gson.annotations.SerializedName

data class OtpResponseModel (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("otp"     ) var otp     : Int?     = null

)