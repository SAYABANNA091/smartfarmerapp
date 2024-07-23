package com.soni.services.web


import com.soni.services.web.models.*
import com.soni.utils.Const
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiCall {
    //App API
    @GET(Const.API.API_GET_STATIC_URLS)
    fun getStaticUls(): Call<StaticUrlsResponse>

    @POST(Const.API.API_SIGN_UP)
    fun postSignUp(@Body param: HashMap<String, String>): Call<OtpResponseModel>

    @POST(Const.API.API_VERIFY_OTP)
    fun postVerifyOtp(@Body param: HashMap<String, String>): Call<OtpResponseModel>

    @POST(Const.API.API_VERIFY_OTP)
    fun postVerifyOtpSignUP(@Body param: HashMap<String, String>): Call<UserLogin>

    @POST(Const.API.API_SET_LANGUAGE)
    fun postSetLanguage(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_SIGN_UP)
    fun postSigIn(@Body param: HashMap<String, String>): Call<OtpResponseModel>

    @POST(Const.API.API_FORGOT_PASSWORD)
    fun postForgotPassword(@Body param: HashMap<String, String>): Call<OtpResponseModel>

    @POST(Const.API.API_CHANGE_PASSWORD)
    fun postChangePassword(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_RESET_PASSWORD)
    fun postResetPassword(@Body param: HashMap<String, String>): Call<OtpResponseModel>

    @POST(Const.API.API_GET_USER)
    fun postGetUser(): Call<GetUser>

    @POST(Const.API.API_MY_PROPERTY)
    fun postGetMyProperty(@Body param: HashMap<String, String>): Call<UserPopertiesModel>

    @POST(Const.API.API_DELETE_PROPERTY)
    fun postDeleteProperty(@Body param: HashMap<String, String>): Call<BaseModel>

    @Multipart
    @POST(Const.API.API_EDIT_USER)
    fun postEditUser(
        @Part image: MultipartBody.Part?,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): Call<GetUser>


    @POST(Const.API.API_DELETE_USER)
    fun postDeleteUser(): Call<BaseModel>

    @POST(Const.API.API_LOGOUT)
    fun postLogoutUser(): Call<BaseModel>

    @Multipart
    @POST(Const.API.API_ADD_EDIT_EMPLOYEE)
    fun postAddEditEmployee(
        @Part tractor_assets: MultipartBody.Part?,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): Call<BaseModel>

    @POST(Const.API.API_ADD_ATTENDANCE_FILTER)
    fun postGetAttendanceFilter(@Body param: HashMap<String, String>): Call<AddAttendanceFilterResponse>

    @POST(Const.API.API_ADD_ATTENDANCE)
    fun postAddAttendance(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_EMPLOYEE_ATTENDANCE)
    fun postEmployeeAttendance(@Body param: HashMap<String, String>): Call<AttendanceResponse>

    @POST(Const.API.API_PROPERTY_TYPES)
    fun postGetPropertyTypes(): Call<PropertyTypeResponse>

    @POST(Const.API.API_INQUIRY_LIST)
    fun postInquiryList(@Body param: HashMap<String, String>): Call<InquiryListResponse>

    @POST(Const.API.API_PRODUCT_INQUIRY_LIST)
    fun postProductInquiryList(@Body param: HashMap<String, String>): Call<GetProductListResponse>

    @POST(Const.API.API_SEARCH_PROPERTY)
    fun postSearchProperty(@Body param: HashMap<String, String>): Call<PropertySearchResponse>

    @POST(Const.API.API_ADD_INQUIRY)
    fun postAddInquiry(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_DELETE_PROPERTY_IMAGE)
    fun postDeleteImage(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_DELETE_EMPLOYEE)
    fun postDeleteEmployee(@Body param: HashMap<String, String>): Call<BaseModel>

    @POST(Const.API.API_LIST_EMPLOYEE)
    fun postListEmployee(@Body param: HashMap<String, String>): Call<EmployeeListResponse>

    @POST(Const.API.API_VIEW_EMPLOYEE_ATT)
    fun postListEmployeeAtt(@Body param: HashMap<String, String>): Call<EmployeeAttListResponse>

    @Multipart
    @POST(Const.API.API_ADD_EDIT_PROPERTY)
    fun postAddEditProperty(
        @Part tractor_assets: Array<MultipartBody.Part?>?,
        @Part jcb_assets: Array<MultipartBody.Part?>?,
        @Part farm_assets: Array<MultipartBody.Part?>?,
        @Part nursery_assets: Array<MultipartBody.Part?>?,
        @Part vermicompost_assets: Array<MultipartBody.Part?>?,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<BaseModel>?


    @POST(Const.API.API_GET_PRODUCT_CATEGORIES)
    fun postGetProductsTypes(): Call<ProductCategoryResponse>

    @POST(Const.API.API_GET_PRODUCT_LIST)
    fun postGetProductsList(@Body param: HashMap<String, String>): Call<GetProductListResponse>

    @POST(Const.API.API_FAV_PRODUCT_LIST)
    fun postGetFavProductsList(@Body param: HashMap<String, String>): Call<GetProductListResponse>

    @Multipart
    @POST(Const.API.API_ADD_EDIT_PRODUCT)
    fun postAddEditProduct(
        @Part product_assets: Array<MultipartBody.Part?>?,
        @Part profile_image: MultipartBody.Part?,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<BaseModel>?

    @POST(Const.API.API_ADD_REMOVE_PRODUCT_FAV)
    fun postAddRemoveProductFav(@Body param: HashMap<String, String>): Call<BaseModel>?

    @POST(Const.API.API_ADD_PRODUCT_INQUIRY)
    fun postAddProductInquiry(@Body param: HashMap<String, String>): Call<BaseModel>?

    @POST(Const.API.API_UPDATE_INQUIRY_STATUS)
    fun postUpdateProductInquiry(@Body param: HashMap<String, String>): Call<BaseModel>?

    @POST(Const.API.API_HOME_INQUIRY_LIST)
    fun postHomeInquiryLis(@Body param: HashMap<String, String>): Call<HomeInquiryDataResponse>?

    @POST(Const.API.API_PRODUCT_DELETE)
    fun postDeleteProduct(@Body param: HashMap<String, String>): Call<BaseModel>?

    @POST(Const.API.API_GET_STATE_LIST)
    fun postGetStateList(@Body param: HashMap<String, String>): Call<StateListResponse>?

    @POST(Const.API.API_GET_DISTRICT_LIST)
    fun postGetDistrictList(@Body param: HashMap<String, String>): Call<DistrictListResponse>?

    @POST(Const.API.API_GET_TALUKA_LIST)
    fun postGetTalukaList(@Body param: HashMap<String, String>): Call<TalukaListResponse>?

    @POST(Const.API.API_GET_MARKET_LIST)
    fun postGetMarketListList(@Body param: HashMap<String, String>): Call<DistrictListResponse>?

    @POST(Const.API.API_GET_COMMODITY_LIST)
    fun postGetCommodityList(@Body param: HashMap<String, String>): Call<CommodityListResponse>?

    @POST(Const.API.API_GET_MARKET)
    fun postMarketDataList(@Body param: HashMap<String, String>): Call<MarketDataListResponse>?


    @POST(Const.API.API_GET_MARKET_GRAPH_DATA)
    fun postMarketGraphDataList(@Body param: HashMap<String, String>): Call<MarketDataListResponse>?


    @POST(Const.API.API_GENERAL_STATE_LIST)
    fun postGeneralStateList(@Body param: HashMap<String, String>): Call<StateListResponse>?

    @POST(Const.API.API_GENERAL_DISTRICT_LIST)
    fun postGeneralDistrictList(@Body param: HashMap<String, String>): Call<DistrictListResponse>?

    @POST(Const.API.API_GENERAL_TALUKA_LIST)
    fun postGeneralTalukaList(@Body param: HashMap<String, String>): Call<TalukaListResponse>?


    //Weather Api
    @GET(Const.API.API_GET_HOURLY_FORCAST+"/{locationKey}"+"?apikey=${Const.API.WeatherApiKey}&language=en-us&details=true&metric=true")
    fun getHourlyForecast(@Path(value = "locationKey", encoded = true)  locationKey:String): Call<ArrayList<HourlyForecast>>

    @GET(Const.API.API_GET_WEEK_FORCAST+"/{locationKey}"+"?apikey=${Const.API.WeatherApiKey}&language=en-us&details=true&metric=true")
    fun getWeekForecast(@Path(value = "locationKey", encoded = true)  locationKey:String): Call<WeekForecast>

    @GET(Const.API.API_GET_LOCATION_CODE+"?apikey=${Const.API.WeatherApiKey}&language=en-us")
    fun getLocationCode(@Query("q") locationKey: String): Call<LocationIdResponse>

    @GET(Const.API.API_GET_LOCATION_LIST+"?apikey=${Const.API.WeatherApiKey}&language=en-us")
    fun getLocationList(@Query("q") locationKey: String): Call<ArrayList<LocationSearchResult>>




}