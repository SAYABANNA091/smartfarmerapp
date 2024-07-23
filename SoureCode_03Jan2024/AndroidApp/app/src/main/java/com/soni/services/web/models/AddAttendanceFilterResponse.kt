package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class AddAttendanceFilterResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("data"        ) var data        : ArrayList<EmployeeData> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

):java.io.Serializable

data class EmployeeData (

    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("employee_name"   ) var employeeName   : String? = null,
    @SerializedName("profile_picture" ) var profilePicture : String? = null,
    @SerializedName("employee_id"     ) var employeeId     : String? = null,
    @SerializedName("employee_email"  ) var employeeEmail  : String? = null,
    @SerializedName("mobile_no"       ) var mobileNo       : String? = null,
    @SerializedName("department"      ) var department     : String? = null,
    @SerializedName("created_by"      ) var createdBy      : Int?    = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("deleted_at"      ) var deletedAt      : String? = null,
    @SerializedName("is_attend"       ) var isAttend       : Int?    = null,
    @SerializedName("isSelected"      ) var isSelected     : Boolean?= false,
    @SerializedName("attendance"      ) var attendance     : ArrayList<Attendance> = arrayListOf(),

):java.io.Serializable


data class Attendance (

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("employee_id" ) var employeeId : Int?    = null,
    @SerializedName("attend_date" ) var attendDate : String? = null,
    @SerializedName("created_at"  ) var createdAt  : String? = null,
    @SerializedName("deleted_at"  ) var deletedAt  : String? = null

):java.io.Serializable

data class AttendanceData (

    @SerializedName("employee_details" ) var employeeDetails : EmployeeData? = EmployeeData()

):java.io.Serializable

data class AttendanceResponse (

    @SerializedName("status" ) var status : Boolean? = null,
    @SerializedName("data"   ) var data   : AttendanceData?    = AttendanceData()

)