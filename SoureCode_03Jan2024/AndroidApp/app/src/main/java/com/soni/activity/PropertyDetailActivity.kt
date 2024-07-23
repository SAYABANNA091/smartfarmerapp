package com.soni.activity

import android.app.Dialog
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.make.dots.dotsindicator.DotsIndicator
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.HomeActivity.Companion.openEditProperty
import com.soni.adapter.PropertyImagesAdapter
import com.soni.databinding.ActivityPropertyDetailBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.Propertyassets
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropertyDetailActivity : BaseActivity() {
    lateinit var binding: ActivityPropertyDetailBinding
    var propertyType: Int = -1
    var isOwnProperty: Boolean = false
    lateinit var propertyImagesAdapter: PropertyImagesAdapter
    var propertyModel: PropertyModel? = null
    var imageList = ArrayList<Propertyassets>()
    var propertyContactName:String?=null
    var propertyContactNumber:String?=null
    var propertyContactImage:String?=null
    var isFromInquiry: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        propertyType = intent.getIntExtra(Const.IntentKey.PropertyType, -1)
        isOwnProperty = intent.getBooleanExtra(Const.IntentKey.isOwnProperty, false)
        isFromInquiry =intent.getBooleanExtra(Const.IntentKey.isFromInquiry, false)
        propertyModel = intent.getSerializableExtra(Const.IntentKey.Property) as PropertyModel?
        propertyContactName = intent.getStringExtra(Const.IntentKey.PropertyContactName)
        propertyContactNumber = intent.getStringExtra(Const.IntentKey.PropertyContactNumber)
        propertyContactImage = intent.getStringExtra(Const.IntentKey.PropertyContactImage)
        init()
        if (propertyModel != null) {
            setData()
        } else {
            propertyImagesAdapter = PropertyImagesAdapter(this, imageList)
            binding.vpPropertyImages.adapter = propertyImagesAdapter
            binding.dotsIndicator.setViewPager(binding.vpPropertyImages)
            binding.vpPropertyImages.adapter?.registerDataSetObserver(binding.dotsIndicator.dataSetObserver)
        }
    }


    private fun init() {


        binding.ivBack.setOnClickListener { finish() }

        when (propertyType) {
            1 -> {
                binding.tvPropertyType.text = "Tractor"
                binding.llFarm.visibility = View.GONE
                binding.llJcb.visibility = View.GONE
                binding.llNursery.visibility = View.GONE
                binding.llVermicompostHelp.visibility = View.GONE
            }
            3 -> {
                binding.tvPropertyType.text = "Farm"
                binding.llTractor.visibility = View.GONE
                binding.llJcb.visibility = View.GONE
                binding.llNursery.visibility = View.GONE
                binding.llVermicompostHelp.visibility = View.GONE
                binding.tvPropertyPrice.visibility = View.INVISIBLE
                binding.tvPropertyRateType.visibility = View.INVISIBLE
            }
            2 -> {
                binding.tvPropertyType.text = "JCB"
                binding.llFarm.visibility = View.GONE
                binding.llTractor.visibility = View.GONE
                binding.llNursery.visibility = View.GONE
                binding.llVermicompostHelp.visibility = View.GONE
            }
            4 -> {
                binding.tvPropertyType.text = "Nursery"
                binding.llFarm.visibility = View.GONE
                binding.llJcb.visibility = View.GONE
                binding.llTractor.visibility = View.GONE
                binding.llVermicompostHelp.visibility = View.GONE
                binding.tvPropertyPrice.visibility = View.INVISIBLE
                binding.tvPropertyRateType.visibility = View.INVISIBLE
            }
            5 -> {
                binding.tvPropertyType.text = "Vermicompost"
                binding.llFarm.visibility = View.GONE
                binding.llJcb.visibility = View.GONE
                binding.llNursery.visibility = View.GONE
                binding.llTractor.visibility = View.GONE
                binding.tvPropertyPrice.visibility = View.INVISIBLE
                binding.tvPropertyRateType.visibility = View.INVISIBLE
                binding.tvPropertyPrice.visibility = View.INVISIBLE
                binding.tvPropertyRateType.visibility = View.INVISIBLE
            }
            else -> {
                binding.llFarm.visibility = View.GONE
                binding.llTractor.visibility = View.GONE
                binding.llJcb.visibility = View.GONE
                binding.llNursery.visibility = View.GONE
                binding.llVermicompostHelp.visibility = View.GONE
                binding.ivLocation.visibility = View.GONE
                binding.tvPropertyLocation.visibility = View.GONE
            }
        }
        if (isOwnProperty) {
            binding.tvInquiry.background=getDrawable(R.drawable.red_btn_rounded_5sp)
            binding.tvShimmer.background=getDrawable(R.drawable.red_btn_rounded_5sp)
            binding.tvInquiry.text="Remove Property"
            if(isFromInquiry){
                binding.rlInquiryBtn.visibility=View.GONE
            }
        }

        if (isOwnProperty    ){
            if(!isFromInquiry) {
                binding.ivEdit.setOnClickListener {
                    finish()
                    openEditProperty(propertyModel)
                }
            }else{
                binding.ivEdit.visibility=View.GONE
            }
        }else{
            binding.ivEdit.visibility=View.GONE
        }
        binding.tvInquiry.setOnClickListener {
            if(isOwnProperty){
                showLanguageDialog()
            }
            else {
                showInquiryPopup()
            }
        }

//        Blurry.with(this).onto(binding.llPropertyType)


    }

    fun setData() {

        imageList = propertyModel!!.propertyassets!!
        propertyImagesAdapter = PropertyImagesAdapter(this, imageList)

        binding.vpPropertyImages.adapter = propertyImagesAdapter
        binding.dotsIndicator.setViewPager(binding.vpPropertyImages)
        binding.vpPropertyImages.adapter?.registerDataSetObserver(binding.dotsIndicator.dataSetObserver)

        binding.tvContactName.text=propertyContactName
        binding.tvContactNumber.text=propertyContactNumber

        if (!propertyContactImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(propertyContactImage)
                .placeholder(R.drawable.logo)
                .error(R.drawable.rafiki)
                .into(binding.ivEmployeePic)
        }else{
            Glide.with(this)

                .load(R.drawable.logo)
                .error(R.drawable.rafiki)
                .into(binding.ivEmployeePic)
        }

        when (propertyType) {
            1 -> {
                binding.propertyName.text = propertyModel!!.tractorTitle
                binding.tvTrackterName.text = propertyModel!!.tractorName
                binding.tvTrackterHorsepower.text = if(propertyModel!!.tractorHoursePower.toString()== "0")"-" else propertyModel!!.tractorHoursePower.toString()
                binding.tvPropertyPrice.text =if(propertyModel!!.tractorPricePerHour.toString().split(".")[0] == "0" )"NA" else "Rs.${propertyModel!!.tractorPricePerHour.toString().split(".")[0]}"
                binding.tvDesc.text = propertyModel!!.tractorDescription
                if(propertyModel!!.tractorVillageName!! == "-" && propertyModel!!.tractorPincode!! == "-"){
                    binding.tvPropertyLocation.text= getString(R.string.location_not_available)
                }else{
                    binding.tvPropertyLocation.text = "${propertyModel!!.tractorVillageName}, ${propertyModel!!.tractorPincode}"
                }
            }
            2 -> {
                binding.propertyName.text = propertyModel!!.jcbTitle
                binding.tvJcbName.text = propertyModel!!.jcbName
                binding.tvJcbHorsepower.text = if(propertyModel!!.jcbHoursePower.toString()== "0")"-" else propertyModel!!.jcbHoursePower.toString()
                binding.tvPropertyPrice.text =if(propertyModel!!.jcbPricePerHour.toString().split(".")[0] == "0" )"-" else "Rs.${propertyModel!!.jcbPricePerHour.toString().split(".")[0]}"
                binding.tvDesc.text = propertyModel!!.jcbDescription
                if(propertyModel!!.jcbVillageName!! == "-" && propertyModel!!.jcbVillageName!! == "-"){
                    binding.tvPropertyLocation.text= getString(R.string.location_not_available)
                }else{
                    binding.tvPropertyLocation.text = "${propertyModel!!.jcbVillageName}, ${propertyModel!!.jcbVillageName}"
                }

            }
            3 -> {
                binding.propertyName.text = propertyModel!!.farmTitle
                binding.tvFarmName.text = propertyModel!!.farmName
                binding.tvFarmHorsepower.text =if(propertyModel!!.farmNoOfAcers!! == "-") "-" else "${propertyModel!!.farmNoOfAcers} Acres"
                binding.tvFarmSurvey.text = propertyModel!!.farmSurveyNo
                binding.tvFarmSurvey.text = propertyModel!!.farmSurveyNo
                binding.tvFarmPincode.text = propertyModel!!.farmPincode
                binding.tvDesc.text = propertyModel!!.farmDescription
                if(propertyModel!!.farmVillegeName!! == "-" && propertyModel!!.farmPincode!! == "-"){
                    binding.tvPropertyLocation.text= getString(R.string.location_not_available)
                }else{
                    binding.tvPropertyLocation.text = "${propertyModel!!.farmVillegeName}, ${propertyModel!!.farmPincode}"
                }

            }
            4 -> {
                binding.propertyName.text = propertyModel!!.nurseryTitle
                binding.tvNurseryName.text = propertyModel!!.nurseryName
                binding.tvNurseryPincode.text = propertyModel!!.nurseryPincode
                binding.tvDesc.text = propertyModel!!.nurseryDescription
                binding.tvPropertyLocation.text= propertyModel!!.nurseryVillageName
                if(propertyModel!!.nurseryVillageName!! == "-" && propertyModel!!.nurseryPincode!! == "-"){
                    binding.tvPropertyLocation.text= getString(R.string.location_not_available)
                }else{
                    binding.tvPropertyLocation.text = "${propertyModel!!.nurseryVillageName}, ${propertyModel!!.nurseryPincode}"
                }

            }
            5 -> {
                binding.propertyName.text = propertyModel!!.vermicompostTitle
                binding.tvVermicompostName.text = propertyModel!!.vermicompostName
                binding.tvVermicompostPincode.text = propertyModel!!.vermicompostPincode
                binding.tvDesc.text = propertyModel!!.vermicompostDescription
                if(propertyModel!!.vermicompostVillageName!! == "-" && propertyModel!!.vermicompostPincode!! == "-"){
                    binding.tvPropertyLocation.text= getString(R.string.location_not_available)
                }else{
                    binding.tvPropertyLocation.text = "${propertyModel!!.vermicompostVillageName}, ${propertyModel!!.vermicompostPincode}"
                }


            }
            else -> {

            }
        }

    }

    private fun callApi(
        name: String,
        email: String,
        mobile: String,
        shimmer: ShimmerFrameLayout,
        txtOk:TextView,
        dialog: Dialog
    ) {
        if (isInternetAvailable(this)) {
            shimmer.visibility=View.VISIBLE
            txtOk.visibility=View.GONE
            var param: HashMap<String, String> = HashMap()

            param["property_id"] = propertyModel!!.id.toString()
            param["name"] = name
            param["email"] = email
            param["mobile_no"] = mobile


            SoniApp.mApiCall?.postAddInquiry(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        showToast(responseLogin.message!!)
                                        shimmer.visibility=View.GONE
                                        txtOk.visibility=View.VISIBLE
                                        dialog.dismiss()
                                        finish()
                                    }
                                } else {
                                    shimmer.visibility=View.GONE
                                    txtOk.visibility=View.VISIBLE

                                    showToast(responseLogin.message!!)
                                    Log.d("SPLASH ERROR", "DATA Not Found")

                                }
                            }
                        }
                    }
                })

        } else {
            showToast(getString(R.string.internet_error))
            shimmer.visibility=View.GONE
            txtOk.visibility=View.VISIBLE

        }


    }

    fun showInquiryPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_inquiry_pop);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._300sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtOk = dialog.findViewById<TextView>(R.id.txt_ok)
        val ll_name = dialog.findViewById<LinearLayout>(R.id.ll_name)
        val et_name = dialog.findViewById<EditText>(R.id.et_name)
        val ll_email = dialog.findViewById<LinearLayout>(R.id.ll_email)
        val et_email = dialog.findViewById<EditText>(R.id.et_email)
        val ll_number = dialog.findViewById<LinearLayout>(R.id.ll_number)
        val et_number = dialog.findViewById<EditText>(R.id.et_number)
        val sf_shimmer = dialog.findViewById<ShimmerFrameLayout>(R.id.sf_shimmer)

        et_name.setText(if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else ""+" "+if (SoniApp.user!!.lastName != null) SoniApp.user!!.lastName else "")
        if (SoniApp.user!!.email.toString()!="null"||SoniApp.user!!.email.toString().isEmpty()) {
            et_email.setText(SoniApp.user!!.email!!)
        }else{
            ll_email.visibility=View.GONE
        }
        et_number.setText(SoniApp.user!!.phoneNumber!!)
        et_name.isEnabled=false
        et_name.isClickable=false

        et_email.isEnabled=false
        et_email.isClickable=false

        et_number.isEnabled=false
        et_number.isClickable=false


        et_name.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_name, b)
        }
        et_email.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_email, b)
        }
        et_number.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_number, b)
        }

        txtOk.setOnClickListener {
//            if (et_name.text.toString().trim().isEmpty()) {
//                showToast(getString(R.string.add_your_name))
//            } else if (et_email.text.toString().trim().isNotEmpty()&& !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString().trim() )
//                    .matches()
//            ) {
//                showToast(getString(R.string.invalid_email))
//            } else if (et_number.text.toString().trim().isEmpty()) {
//                showToast(getString(R.string.add_mobile_number))
//            } else if (et_number.text.toString().trim().length < 10) {
//                showToast(getString(R.string.invalid_phone_number))
//            } else {
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
                callApi(
                    et_name.text.toString().trim(),
                    et_email.text.toString().trim(),
                    et_number.text.toString(),
                    sf_shimmer, txtOk,dialog
                )
//            }
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()

    }


    private fun callDeleteApi(id:Int) {

        if (isInternetAvailable(this)) {

            var param: HashMap<String, String> = HashMap()
            param["id"] = id.toString()

            SoniApp.mApiCall?.postDeleteProperty(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                       showToast(getString(R.string.api_error))
                        binding.tvInquiry.visibility = View.GONE
                        binding.tvShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                binding.tvInquiry.visibility = View.GONE
                                binding.tvShimmer.visibility = View.GONE
                                finish()
                                showToast(responseLogin.message!!)

                            }
                        }
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.tvInquiry.visibility = View.GONE
            binding.tvShimmer.visibility = View.GONE
        }
    }

    private fun showLanguageDialog(){

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._150sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtyes = dialog.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog.findViewById<TextView>(R.id.tv_no)
        txtyes.setOnClickListener {
            dialog.dismiss()

            callDeleteApi(propertyModel!!.id!!)
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }
}