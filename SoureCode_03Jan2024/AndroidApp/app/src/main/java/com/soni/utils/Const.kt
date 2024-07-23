package com.soni.utils

import com.soni.BuildConfig

class Const {
    object TAB {
        const val home = "HOME"
        const val rent = "RENT"
        const val profile = "PROFILE"
        const val buySell = "BUYSELL"

    }
    object IntentKey {
        const val isEdit = "isEdit"
        const val PropertyType = "PropertyType"
        const val PropertySearch = "PropertySearch"
        const val PropertyContactName = "PropertyContactName"
        const val PropertyContactNumber = "PropertyContactNumber"
        const val PropertyContactImage = "PropertyContactImage"
        const val Property = "Property"
        const val PropertyID = "PropertyID"
        const val ProductID = "ProductID"
        const val ProductData = "ProductData"
        const val isOwnProperty = "isOwnProperty"
        const val isFromInquiry = "isFromInquiry"
        const val first_name = "first_name"
        const val last_name = "last_name"
        const val password = "password"
        const val state = "state"
        const val district = "district"
        const val taluka = "taluka"
        const val village = "village"
        const val pincode = "pincode"
        const val Email = "Email"
        const val PhoneNumber = "PhoneNumber"
        const val OTP = "OTP"
        const val Unauthorized = "Unauthorized"
        const val Employee = "Employee"
        const val State = "State"
        const val Commodity = "Commodity"
        const val Varity = "Varity"
        const val Market = "Market"
        const val Difference = "Difference"
        const val isFromNotification = "isFromNotification"
        const val notificationId = "NotificationId"
        const val FROM_CHANGE_LANG = "FROM_CHANGE_LANG"

    }
    companion object {

    }

    object Language {
        const val ENGLISH = "en"
        const val KANNADA = "kn"
    }

    object ApiHeaders {
        const val DEVICE_ID = "deviceId"
        const val DEVICE_TYPE = "deviceTypeId"
        const val OS_VERSION = "OSVersion"
        const val DEVICE_NAME = "DeviceName"
        const val USER_TOKEN = "userToken"
        const val USER_ID = "userId"
        const val LANGUAGE_ID = "languageId"
        const val DeviceToken = "deviceToken"
        const val DEVICE_TYPE_ANDROID = "1"
        const val Latitude = "Latitude"
        const val Longitude = "Longitude"

    }


    object API {
        const val WeatherApiKey = "3IY1KTrI7snjCMTmS457PYiBquP9qyWL"
        const val BaseUrl = "${BuildConfig.BASE_URL}"
        const val API_SIGN_UP = "${BaseUrl}api/sign-up"
        const val API_SIGN_IN = "${BaseUrl}api/sign-In"
        const val API_GET_USER = "${BaseUrl}api/get-profile-details"
        const val API_EDIT_USER = "${BaseUrl}api/edit-profile"
        const val API_DELETE_USER = "${BaseUrl}api/delete-account"
        const val API_LOGOUT = "${BaseUrl}api/logout"
        const val API_CHANGE_PASSWORD = "${BaseUrl}api/change-password"
        const val API_VERIFY_OTP = "${BaseUrl}api/verifyOtp"
        const val API_FORGOT_PASSWORD = "${BaseUrl}api/forgot-password"
        const val API_RESET_PASSWORD = "${BaseUrl}api/reset-password"
        const val API_SET_LANGUAGE = "${BaseUrl}api/set-language-preferences"
        const val API_ADD_EDIT_PROPERTY = "${BaseUrl}api/add-edit-property"
        const val API_MY_PROPERTY = "${BaseUrl}api/my-property"
        const val API_DELETE_PROPERTY = "${BaseUrl}api/delete-property"
        const val API_DELETE_PROPERTY_IMAGE = "${BaseUrl}api/delete-property-picture"
        const val API_ADD_EDIT_EMPLOYEE = "${BaseUrl}api/add-edit-employee"
        const val API_ADD_ATTENDANCE_FILTER = "${BaseUrl}api/add-attendance-filter"
        const val API_ADD_ATTENDANCE = "${BaseUrl}api/add-employee-attendance"
        const val API_EMPLOYEE_ATTENDANCE = "${BaseUrl}api/employee-details"
        const val API_PROPERTY_TYPES = "${BaseUrl}api/property-types"
        const val API_INQUIRY_LIST = "${BaseUrl}api/inquiry-list"
        const val API_SEARCH_PROPERTY = "${BaseUrl}api/search-property"
        const val API_ADD_INQUIRY = "${BaseUrl}api/add-inquiry"
        const val API_GET_STATIC_URLS = "${BaseUrl}api/static-urls"
        const val API_DELETE_EMPLOYEE = "${BaseUrl}api/delete-employee"
        const val API_LIST_EMPLOYEE = "${BaseUrl}api/employee-list"
        const val API_VIEW_EMPLOYEE_ATT = "${BaseUrl}api/view-attendance-filter"
        const val API_GET_PRODUCT_LIST = "${BaseUrl}api/product-list"
        const val API_ADD_EDIT_PRODUCT = "${BaseUrl}api/add-edit-product"
        const val API_GET_PRODUCT_CATEGORIES = "${BaseUrl}api/category-product-list"
        const val API_ADD_REMOVE_PRODUCT_FAV = "${BaseUrl}api/favourites-product"
        const val API_FAV_PRODUCT_LIST = "${BaseUrl}api/favourites-product-list"
        const val API_ADD_PRODUCT_INQUIRY = "${BaseUrl}api/add-product-inquiries"
        const val API_UPDATE_INQUIRY_STATUS = "${BaseUrl}api/update-product-inquiry-status"
        const val API_HOME_INQUIRY_LIST = "${BaseUrl}api/home-inquiry-list"
        const val API_PRODUCT_INQUIRY_LIST = "${BaseUrl}api/product-inquiry-list"
        const val API_PRODUCT_DELETE = "${BaseUrl}api/delete-product"
        const val API_GET_STATE_LIST = "${BaseUrl}api/market-state-list"
        const val API_GET_DISTRICT_LIST = "${BaseUrl}api/market-district-list"
        const val API_GET_TALUKA_LIST = "${BaseUrl}api/market-taluka-list"
        const val API_GET_MARKET_LIST = "${BaseUrl}api/depedent-state-market"
        const val API_GET_COMMODITY_LIST = "${BaseUrl}api/market-commodity-list"
        const val API_GET_MARKET = "${BaseUrl}api/get-market-api-data"
        const val API_GET_MARKET_GRAPH_DATA = "${BaseUrl}api/get-market-graph-data"
        const val API_GENERAL_STATE_LIST = "${BaseUrl}api/user-state-list"
        const val API_GENERAL_DISTRICT_LIST = "${BaseUrl}api/user-district-list"
        const val API_GENERAL_TALUKA_LIST = "${BaseUrl}api/user-taluka-list"


        //Weather Api
        const val API_GET_HOURLY_FORCAST = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/"
        const val API_GET_WEEK_FORCAST = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/"
        const val API_GET_LOCATION_CODE = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search"
        const val API_GET_LOCATION_LIST = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete"
        const val API_PHOTO = "${BaseUrl}"
    }

}