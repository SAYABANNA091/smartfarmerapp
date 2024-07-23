package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class StaticUrlsResponse (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("data"    ) var data    : StaticUrls?    = StaticUrls(),
    @SerializedName("message" ) var message : String?  = null

)

data class StaticUrls (

    @SerializedName("privacy-policy-url"   ) var privacy_policy_url   : String? = null,
    @SerializedName("terms-conditions-url" ) var terms_conditions_url : String? = null,
    @SerializedName("contact-us-url"       ) var contact_us_url       : String? = null,
    @SerializedName("support-url"          ) var support_url          : String? = null

):java.io.Serializable