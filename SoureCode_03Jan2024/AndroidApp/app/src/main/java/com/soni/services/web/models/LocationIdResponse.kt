package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class LocationIdResponse (

    @SerializedName("Version"                ) var Version                : Int?                              = null,
    @SerializedName("Key"                    ) var Key                    : String?                           = null,
    @SerializedName("Type"                   ) var Type                   : String?                           = null,
    @SerializedName("Rank"                   ) var Rank                   : Int?                              = null,
    @SerializedName("LocalizedName"          ) var LocalizedName          : String?                           = null,
    @SerializedName("EnglishName"            ) var EnglishName            : String?                           = null,
    @SerializedName("PrimaryPostalCode"      ) var PrimaryPostalCode      : String?                           = null,
    @SerializedName("Region"                 ) var Region                 : Region?                           = Region(),
    @SerializedName("Country"                ) var Country                : Country?                          = Country(),
    @SerializedName("AdministrativeArea"     ) var AdministrativeArea     : AdministrativeArea?               = AdministrativeArea(),
    @SerializedName("TimeZone"               ) var TimeZone               : TimeZone?                         = TimeZone(),
    @SerializedName("GeoPosition"            ) var GeoPosition            : GeoPosition?                      = GeoPosition(),
    @SerializedName("IsAlias"                ) var IsAlias                : Boolean?                          = null,
    @SerializedName("SupplementalAdminAreas" ) var SupplementalAdminAreas : ArrayList<SupplementalAdminAreas> = arrayListOf(),
    @SerializedName("DataSets"               ) var DataSets               : ArrayList<String>                 = arrayListOf()

)

data class LocationSearchResult (

    @SerializedName("Version"            ) var Version            : Int?                = null,
    @SerializedName("Key"                ) var Key                : String?             = null,
    @SerializedName("Type"               ) var Type               : String?             = null,
    @SerializedName("Rank"               ) var Rank               : Int?                = null,
    @SerializedName("LocalizedName"      ) var LocalizedName      : String?             = null,
    @SerializedName("Country"            ) var Country            : Country?            = Country(),
    @SerializedName("AdministrativeArea" ) var AdministrativeArea : AdministrativeArea? = AdministrativeArea()

)

data class Region (

    @SerializedName("ID"            ) var ID            : String? = null,
    @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
    @SerializedName("EnglishName"   ) var EnglishName   : String? = null

)
data class Country (

    @SerializedName("ID"            ) var ID            : String? = null,
    @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
    @SerializedName("EnglishName"   ) var EnglishName   : String? = null

)

data class AdministrativeArea (

    @SerializedName("ID"            ) var ID            : String? = null,
    @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
    @SerializedName("EnglishName"   ) var EnglishName   : String? = null,
    @SerializedName("Level"         ) var Level         : Int?    = null,
    @SerializedName("LocalizedType" ) var LocalizedType : String? = null,
    @SerializedName("EnglishType"   ) var EnglishType   : String? = null,
    @SerializedName("CountryID"     ) var CountryID     : String? = null

)

data class TimeZone (

    @SerializedName("Code"             ) var Code             : String?  = null,
    @SerializedName("Name"             ) var Name             : String?  = null,
    @SerializedName("GmtOffset"        ) var GmtOffset        : Double?  = null,
    @SerializedName("IsDaylightSaving" ) var IsDaylightSaving : Boolean? = null,
    @SerializedName("NextOffsetChange" ) var NextOffsetChange : String?  = null

)
data class Metric (

    @SerializedName("Value"    ) var Value    : Int?    = null,
    @SerializedName("Unit"     ) var Unit     : String? = null,
    @SerializedName("UnitType" ) var UnitType : Int?    = null

)
data class Imperial (

    @SerializedName("Value"    ) var Value    : Int?    = null,
    @SerializedName("Unit"     ) var Unit     : String? = null,
    @SerializedName("UnitType" ) var UnitType : Int?    = null

)


data class Elevation (

    @SerializedName("Metric"   ) var Metric   : Metric?   = Metric(),
    @SerializedName("Imperial" ) var Imperial : Imperial? = Imperial()

)
data class GeoPosition (

    @SerializedName("Latitude"  ) var Latitude  : Double?    = null,
    @SerializedName("Longitude" ) var Longitude : Double?    = null,
    @SerializedName("Elevation" ) var Elevation : Elevation? = Elevation()

)
data class SupplementalAdminAreas (

    @SerializedName("Level"         ) var Level         : Int?    = null,
    @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
    @SerializedName("EnglishName"   ) var EnglishName   : String? = null

)