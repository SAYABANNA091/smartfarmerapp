package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class UserPopertiesModel (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("data"    ) var data    : ArrayList<PropertyModel> = arrayListOf(),
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("totalpage" ) var totalpage : Int?         = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?         = null,

):java.io.Serializable
data class PropertyModel (

    @SerializedName("id"                        ) var id                      : Int?    = null,
    @SerializedName("property_id"               ) var propertyId              : Int?    = null,
    @SerializedName("name"                      ) var name                    : String? = null,
    @SerializedName("user_id"                   ) var userId                  : Int?    = null,
    @SerializedName("property_type_id"          ) var propertyTypeId          : Int?    = null,
    @SerializedName("tractor_title"             ) var tractorTitle            : String? = null,
    @SerializedName("tractor_name"              ) var tractorName             : String? = null,
    @SerializedName("tractor_hourse_power"      ) var tractorHoursePower      : Int?    = null,
    @SerializedName("tractor_village_name"      ) var tractorVillageName      : String? = null,
    @SerializedName("tractor_price_per_hour"    ) var tractorPricePerHour     : String? = null,
    @SerializedName("tractor_description"       ) var tractorDescription      : String? = null,
    @SerializedName("jcb_title"                 ) var jcbTitle                : String? = null,
    @SerializedName("jcb_name"                  ) var jcbName                 : String? = null,
    @SerializedName("jcb_hourse_power"          ) var jcbHoursePower          : String? = null,
    @SerializedName("jcb_village_name"          ) var jcbVillageName          : String? = null,
    @SerializedName("jcb_price_per_hour"        ) var jcbPricePerHour         : String? = null,
    @SerializedName("jcb_description"           ) var jcbDescription          : String? = null,
    @SerializedName("farm_title"                ) var farmTitle               : String? = null,
    @SerializedName("farm_name"                 ) var farmName                : String? = null,
    @SerializedName("farm_no_of_acers"          ) var farmNoOfAcers           : String? = null,
    @SerializedName("farm_survey_no"            ) var farmSurveyNo            : String? = null,
    @SerializedName("farm_villege_name"         ) var farmVillegeName         : String? = null,
    @SerializedName("farm_pincode"              ) var farmPincode             : String? = null,
    @SerializedName("farm_description"          ) var farmDescription         : String? = null,
    @SerializedName("nursery_title"             ) var nurseryTitle            : String? = null,
    @SerializedName("nursery_name"              ) var nurseryName             : String? = null,
    @SerializedName("nursery_village_name"      ) var nurseryVillageName      : String? = null,
    @SerializedName("nursery_pincode"           ) var nurseryPincode          : String? = null,
    @SerializedName("nursery_description"       ) var nurseryDescription      : String? = null,
    @SerializedName("vermicompost_title"        ) var vermicompostTitle       : String? = null,
    @SerializedName("vermicompost_name"         ) var vermicompostName        : String? = null,
    @SerializedName("vermicompost_village_name" ) var vermicompostVillageName : String? = null,
    @SerializedName("vermicompost_pincode"      ) var vermicompostPincode     : String? = null,
    @SerializedName("tractor_pincode"           ) var tractorPincode          : String? = null,
    @SerializedName("jcb_pincode"               ) var jcbPincode              : String? = null,
    @SerializedName("vermicompost_description"  ) var vermicompostDescription : String? = null,
    @SerializedName("created_at"                ) var createdAt               : String? = null,
    @SerializedName("deleted_at"                ) var deletedAt               : String? = null,
    @SerializedName("first_name"                ) var firstName               : String? = null,
    @SerializedName("last_name"                 ) var lastName                : String? = null,
    @SerializedName("email"                     ) var email                   : String? = null,
    @SerializedName("phone_number"              ) var phoneNumber             : String? = null,
    @SerializedName("profile_url"               ) var profileUrl              : String? = null,
    @SerializedName("password"                  ) var password                : String? = null,
    @SerializedName("language_id"               ) var languageId              : Int?    = null,
    @SerializedName("user_type"                 ) var userType                : Int?    = null,
    @SerializedName("state"                     ) var state                   : String? = null,
    @SerializedName("district"                  ) var district                : String? = null,
    @SerializedName("taluka"                    ) var taluka                  : String? = null,
    @SerializedName("village"                   ) var village                 : String? = null,
    @SerializedName("pincode"                   ) var pincode                 : String? = null,
    @SerializedName("is_active"                 ) var isActive                : Int?    = null,
    @SerializedName("updated_at"                ) var updatedAt               : String? = null,
    @SerializedName("propertyassets"            ) var propertyassets          : ArrayList<Propertyassets> = arrayListOf(),
    @SerializedName("userdetails"               ) var userdetails             : UserModel? = null,

):java.io.Serializable

data class Propertyassets (

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("property_id" ) var propertyId : Int?    = null,
    @SerializedName("asset_url"   ) var assetUrl   : String? = null,
    @SerializedName("created_at"  ) var createdAt  : String? = null,
    @SerializedName("deleted_at"  ) var deletedAt  : String? = null

):java.io.Serializable


data class HomeInquiryData (

    @SerializedName("Product_Inquiry"  ) var ProductInquiry  : ArrayList<ProductData>  = arrayListOf(),
    @SerializedName("Property_Inquiry" ) var PropertyInquiry : ArrayList<PropertyModel> = arrayListOf()

)



data class HomeInquiryDataResponse (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : HomeInquiryData?    = HomeInquiryData()

)