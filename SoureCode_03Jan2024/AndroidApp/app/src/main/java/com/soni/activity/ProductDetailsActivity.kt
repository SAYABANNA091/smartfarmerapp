package com.soni.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.PropertyImagesAdapter
import com.soni.databinding.ActivityProductDetailsBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.ProductData
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.Propertyassets
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding:ActivityProductDetailsBinding
    var propertyType: Int = -1
    var isOwnProperty: Boolean = false
    var isFromInquiry: Boolean = false
    lateinit var propertyImagesAdapter: PropertyImagesAdapter
    var imageList = ArrayList<Propertyassets>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        propertyType = intent.getIntExtra(Const.IntentKey.ProductID, -1)
        isOwnProperty = intent.getBooleanExtra(Const.IntentKey.isOwnProperty, false)
        isFromInquiry=intent.getBooleanExtra(Const.IntentKey.isFromInquiry, false)

        init()

    }

    override fun onResume() {
        super.onResume()
        if (SoniApp.productData==null) {
            SoniApp.productData = intent.getSerializableExtra(Const.IntentKey.ProductData) as ProductData?

            if (SoniApp.productData != null) {
                setData()
            } else {

                propertyImagesAdapter = PropertyImagesAdapter(this, imageList)
                binding.vpPropertyImages.adapter = propertyImagesAdapter
                binding.dotsIndicator.setViewPager(binding.vpPropertyImages)
                binding.vpPropertyImages.adapter?.registerDataSetObserver(binding.dotsIndicator.dataSetObserver)
                binding.tvProductName.text = "Brown Cow"
                binding.tvProductPrice.text = "Rs.2500"
                binding.tvDesc.text = "The Brown Swiss or Braunvieh is light brown in colour with a creamy white muzzle and dark noze, dark-blue eye pigmentation which helps the breed to resist extreme solar radiation."

            } }else{
            setData()

            }


    }

    override fun onDestroy() {
        super.onDestroy()
        SoniApp.productData=null
    }
    private fun init() {


        binding.ivBack.setOnClickListener { finish() }
        if (isOwnProperty){
        binding.ivEdit.setOnClickListener {
            var mIntent = Intent(this, AddEditProductsActivity::class.java)
            mIntent.putExtra(Const.IntentKey.ProductID,propertyType)
            mIntent.putExtra(Const.IntentKey.ProductData,SoniApp.productData)

            startActivity(mIntent)
        }
        }
        else{
            binding.ivEdit.visibility=View.GONE
        }
        binding.tvInquiry.setOnClickListener {
            if(isOwnProperty){
                showDeleteDialog()
            }
           else {
                showInquiryPopup()
            }
        }

//        Blurry.with(this).onto(binding.llPropertyType)


    }

    fun setData() {
        imageList.clear()
        if(SoniApp.productData!!.productassets!!.size>5){
           SoniApp.productData!!.productassets!!.subList(SoniApp.productData!!.productassets!!.size-5,SoniApp.productData!!.productassets!!.size).map {
                imageList.add(it)
            }

        }else{
            imageList = SoniApp.productData!!.productassets!!

        }

        propertyImagesAdapter = PropertyImagesAdapter(this, imageList)

        binding.vpPropertyImages.adapter = propertyImagesAdapter
        binding.dotsIndicator.setViewPager(binding.vpPropertyImages)
        binding.vpPropertyImages.adapter?.registerDataSetObserver(binding.dotsIndicator.dataSetObserver)

        binding.tvContactName.text=SoniApp.productData!!.username
        binding.tvContactNumber.text=SoniApp.productData!!.phoneNumber
        binding.tvProductPincode.text=SoniApp.productData!!.pincode
        binding.tvPropertyType.text = SoniApp.productData!!.categoryName
        if (isOwnProperty) {
            binding.tvInquiry.background=getDrawable(R.drawable.red_btn_rounded_5sp)
            binding.tvShimmer.background=getDrawable(R.drawable.red_btn_rounded_5sp)
            binding.tvInquiry.text=getString(R.string.remove_product)
            if(isFromInquiry){
                binding.rlInquiryBtn.visibility=View.GONE
            }
        }
        if (!SoniApp.productData!!.profileUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(SoniApp.productData!!.profileUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.user_icon)
                .into(binding.ivContactPic)
        }else{
            Glide.with(this)
                .load(R.drawable.user_icon)
                .error(R.drawable.user_icon)
                .into(binding.ivContactPic)
        }
        binding.tvProductName.text = SoniApp.productData!!.title
        binding.tvProductPrice.text = "Rs."+SoniApp.productData!!.price!!.split(".")[0]
        binding.tvDesc.text = SoniApp.productData!!.description


    }

    private fun callApi(
        name: String,
        email: String,
        mobile: String,
        qty: String,
        shimmer: ShimmerFrameLayout,
        txtOk: TextView,
        dialog: Dialog
    ) {
        if (isInternetAvailable(this)) {
            shimmer.visibility= View.VISIBLE
            txtOk.visibility= View.GONE
            var param: HashMap<String, String> = HashMap()

            param["product_id"] = SoniApp.productData!!.id.toString()
            param["name"] = name
            param["email"] = email
            param["mobile_no"] = mobile
            param["description"] = SoniApp.productData!!.description!!.toString()
            param["quantity"] = qty

            SoniApp.mApiCall?.postAddProductInquiry(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                        dialog.setCancelable(true)
                        dialog.setCanceledOnTouchOutside(true)
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
                                        shimmer.visibility= View.GONE
                                        txtOk.visibility= View.VISIBLE
                                        dialog.dismiss()
                                        finish()
                                    }
                                } else {
                                    dialog.setCancelable(true)
                                    dialog.setCanceledOnTouchOutside(true)
                                    shimmer.visibility= View.GONE
                                    txtOk.visibility= View.VISIBLE

                                    showToast(responseLogin.message!!)
                                    Log.d("SPLASH ERROR", "DATA Not Found")

                                }
                            }
                        }
                    }
                })

        } else {
            showToast(getString(R.string.internet_error))
            shimmer.visibility= View.GONE
            txtOk.visibility= View.VISIBLE
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)

        }


    }

    private fun showDeleteDialog(){

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
        val tv_title = dialog.findViewById<TextView>(R.id.tv_title)
        val tv_message = dialog.findViewById<TextView>(R.id.tv_message)

        tv_title.text=getString(R.string.delete_product)
        tv_message.text=getString(R.string.are_you_sure_you_want_to_delete)

        txtyes.setOnClickListener {
            dialog.dismiss()
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            callDeleteApi()
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }

    private fun callDeleteApi(
    ) {
        if (isInternetAvailable(this)) {
            binding.sfShimmer.visibility=View.VISIBLE
            binding.tvInquiry.visibility=View.GONE
            hideKeyboard(binding.root)
            var param: HashMap<String, String> = HashMap()

            param["product_id"] = SoniApp.productData!!.id.toString()

            SoniApp.mApiCall?.postDeleteProduct(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))

                            binding.sfShimmer.visibility=View.GONE
                            binding.tvInquiry.visibility=View.VISIBLE


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
                                        finish()
                                    }
                                } else {
                                    showToast(responseLogin.message!!)
                                    binding.sfShimmer.visibility=View.GONE
                                    binding.tvInquiry.visibility=View.VISIBLE
                                    Log.d("SPLASH ERROR", "DATA Not Found")

                                }
                            }
                        }
                        else {
                            showToast(getString(R.string.api_error))
                            binding.sfShimmer.visibility=View.GONE
                            binding.tvInquiry.visibility=View.VISIBLE

                        }
                    }
                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.sfShimmer.visibility=View.GONE
            binding.tvInquiry.visibility=View.VISIBLE

        }


    }

    fun showInquiryPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_inquiry_product_pop);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._345sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtOk = dialog.findViewById<TextView>(R.id.txt_ok)
        val ll_name = dialog.findViewById<LinearLayout>(R.id.ll_name)
        val et_name = dialog.findViewById<EditText>(R.id.et_name)
        val ll_email = dialog.findViewById<LinearLayout>(R.id.ll_email)
        val et_email = dialog.findViewById<EditText>(R.id.et_email)
        val ll_number = dialog.findViewById<LinearLayout>(R.id.ll_number)
        val et_number = dialog.findViewById<EditText>(R.id.et_number)
        val ll_qty = dialog.findViewById<LinearLayout>(R.id.ll_qty)
        val et_qty = dialog.findViewById<EditText>(R.id.et_qty)
        val sf_shimmer = dialog.findViewById<ShimmerFrameLayout>(R.id.sf_shimmer)

        et_name.setText(if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else ""+" "+ if (SoniApp.user!!.lastName != null) SoniApp.user!!.lastName else "")
        if (SoniApp.user!!.email.toString()!="null"|| SoniApp.user!!.email.toString().isEmpty()) {
            et_email.setText(SoniApp.user!!.email!!)
        }else{
            ll_email.visibility= View.GONE
        }
        et_number.setText(SoniApp.user!!.phoneNumber!!)
        et_name.isEnabled=false
        et_name.isClickable=false

        et_email.isEnabled=false
        et_email.isClickable=false

        et_number.isEnabled=false
        et_number.isClickable=false
        et_qty.requestFocus()

        et_name.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_name, b)
        }
        et_email.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_email, b)
        }
        et_number.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_number, b)
        }
        et_qty.setOnFocusChangeListener { view, b ->
            onFocusChange(ll_qty, b)
        }

        txtOk.setOnClickListener {

            if (et_name.text.toString().trim().isEmpty()) {
                showToast(getString(R.string.add_your_name))
            } else if (et_qty.text.toString().trim().isEmpty())
            {
                showToast(getString(R.string.enter_qty))
            }else if (et_email.text.toString().trim().isNotEmpty()&& !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString().trim() )
                    .matches()
            ) {
                showToast(getString(R.string.invalid_email))
            } else if (et_number.text.toString().trim().isEmpty()) {
                showToast(getString(R.string.add_mobile_number))
            } else if (et_number.text.toString().trim().length < 10) {
                showToast(getString(R.string.invalid_phone_number))
            } else {
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                hideKeyboard(binding.root)
            callApi(
                et_name.text.toString().trim(),
                et_email.text.toString().trim(),
                et_number.text.toString(),
                et_qty.text.toString().trim(),
                sf_shimmer, txtOk,dialog
            )
            }

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()

    }
}