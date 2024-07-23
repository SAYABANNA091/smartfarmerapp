package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class StateListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("message"     ) var message     : String?         = null,
    @SerializedName("data"        ) var data        : ArrayList<State> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)

data class State (

    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null

)



data class DistrictListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("message"     ) var message     : String?         = null,
    @SerializedName("data"        ) var data        : ArrayList<District> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)

data class District (

    @SerializedName("id"        ) var id       : Int?    = null,
    @SerializedName("name"      ) var name     : String? = null,
    @SerializedName("states_id" ) var statesId : Int?    = null

)
data class TalukaListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("message"     ) var message     : String?         = null,
    @SerializedName("data"        ) var data        : ArrayList<Taluka> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)

data class Taluka (

    @SerializedName("id"        ) var id       : Int?    = null,
    @SerializedName("name"      ) var name     : String? = null,
    @SerializedName("districts_id" ) var districtsId : Int?    = null

)

data class CommodityListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("message"     ) var message     : String?         = null,
    @SerializedName("data"        ) var data        : ArrayList<Commodity> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)

data class Commodity (

    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null

)


data class MarketDataListResponse (

    @SerializedName("status"      ) var status      : Boolean?        = null,
    @SerializedName("message"     ) var message     : String?         = null,
    @SerializedName("data"        ) var data        : ArrayList<MarketData> = arrayListOf(),
    @SerializedName("totalpage"   ) var totalpage   : Int?            = null,
    @SerializedName("totalrecord" ) var totalrecord : Int?            = null

)
data class MarketData (

    @SerializedName("state"        ) var state          : String? = null,
    @SerializedName("district"     ) var district       : String? = null,
    @SerializedName("market"       ) var market         : String? = null,
    @SerializedName("commodity"    ) var commodity      : String? = null,
    @SerializedName("variety"      ) var variety        : String? = null,
    @SerializedName("grade"        ) var grade          : String? = null,
    @SerializedName("arrival_date" ) var arrivalDate    : String? = null,
    @SerializedName("min_price"    ) var minPrice       : String? = null,
    @SerializedName("max_price"    ) var maxPrice       : String? = null,
    @SerializedName("modal_price"  ) var modalPrice     : String? = null,
    @SerializedName("compare_rate" ) var compare_rate   : Int? = null,

)