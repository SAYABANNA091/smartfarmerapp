package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class PropertyTypeResponse (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("data"    ) var propertyType    : ArrayList<PropertyType> = arrayListOf(),
    @SerializedName("message" ) var message : String?         = null

)

data class PropertyType (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("name"       ) var name      : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("deleted_at" ) var deletedAt : String? = null,
    @SerializedName("isSelected" ) var isSelected: Boolean?= false,


)
data class ProductType (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("name"       ) var name      : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("deleted_at" ) var deletedAt : String? = null,
    @SerializedName("isSelected" ) var isSelected: Boolean?= false,


)