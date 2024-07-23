package com.soni.services.web.models
import com.google.gson.annotations.SerializedName


data class UserModel (

    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("first_name"   ) var firstName   : String? = "",
    @SerializedName("last_name"    ) var lastName    : String? = "",
    @SerializedName("email"        ) var email       : String? = null,
    @SerializedName("phone_number" ) var phoneNumber : String? = null,
    @SerializedName("profile_url"  ) var profileUrl  : String? = null,
    @SerializedName("language_id"  ) var languageId  : Int?    = null,
    @SerializedName("user_type"    ) var userType    : Int?    = null,
    @SerializedName("state"        ) var state       : String? = "",
    @SerializedName("district"     ) var district    : String? = "",
    @SerializedName("taluka"       ) var taluka      : String? = "",
    @SerializedName("village"      ) var village     : String? = "",
    @SerializedName("pincode"      ) var pincode     : String? = ""

):java.io.Serializable


data class UserLogin (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : UserData?    = UserData()

)


data class UserData (

    @SerializedName("user"  ) var user  : UserModel?   = UserModel(),
    @SerializedName("token" ) var token : String? = null

)
data class GetUser(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("data"    ) var data    : UserModel?    = UserModel(),
    @SerializedName("message" ) var message : String?  = null,
)