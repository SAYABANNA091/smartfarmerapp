package com.soni.services.web.models

import com.google.gson.annotations.SerializedName

class GetProductListResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("data") var data: ArrayList<ProductData> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("totalpage") var totalpage: Int? = null,
    @SerializedName("totalrecord") var totalrecord: Int? = null

)

data class ProductData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("category_name") var categoryName: String? = null,
    @SerializedName("favourite_flag") var favouriteFlag: Int? = null,
    @SerializedName("u_name") var username: String? = null,
    @SerializedName("u_mobile") var phoneNumber: String? = null,
    @SerializedName("u_pincode") var pincode: String? = null,
    @SerializedName("profile_image") var profileUrl: String? = null,
    @SerializedName("inquiry_status") var inquiry_status: String? = null,
    @SerializedName("quantity") var qty: String? = null,
    @SerializedName("inquiry_id"     ) var inquiryId     : Int?                     = null,
    @SerializedName("productassets") var productassets: ArrayList<Propertyassets> = arrayListOf()

) : java.io.Serializable
