package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class EmployeeListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("data"        ) var data        : ArrayList<EmployeeListData> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)
data class EmployeeListData (

    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("employee_name"   ) var employeeName   : String? = null,
    @SerializedName("profile_picture" ) var profilePicture : String? = null,
    @SerializedName("employee_id"     ) var employeeId     : String? = null,
    @SerializedName("employee_email"  ) var employeeEmail  : String? = null,
    @SerializedName("mobile_no"       ) var mobileNo       : String? = null,
    @SerializedName("department"      ) var department     : String? = null,
    @SerializedName("created_by"      ) var createdBy      : Int?    = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("isSelected"      ) var isSelected     : Boolean = false,
    @SerializedName("deleted_at"      ) var deletedAt      : String? = null

)