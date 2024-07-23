package com.soni.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.ImagesAdapter
import com.soni.databinding.ActivityAddEditProductsBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.ProductData
import com.soni.services.web.models.Propertyassets
import com.soni.utils.Const
import com.soni.utils.MapCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddEditProductsActivity : BaseActivity() , MapCallBack {
    lateinit var binding: ActivityAddEditProductsBinding
    var imageList = ArrayList<Propertyassets>()
    lateinit var imagesAdapter: ImagesAdapter
    lateinit var sellerImagesAdapter: ImagesAdapter
    var isDropDownOpen = false
    var productID: String = ""
    var RC_GALLERY_PERM = 100
    var ProductType: Int = -1
    var imgCount = 5
    var isEdit = false
    var productData: ProductData? = null
    var sellerImage = ArrayList<Propertyassets>()
    var isForSeller=false
    var lat:Number?=null
    var long:Number?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityAddEditProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SoniApp.mapCallbacks.add(this@AddEditProductsActivity)
        init()
    }

    override fun onDestroy() {
        SoniApp.mapCallbacks.remove(this@AddEditProductsActivity)
        super.onDestroy()
    }

    private fun init() {
        if(SoniApp.location!=null) {
            lat = SoniApp.location!!.latitude
            long = SoniApp.location!!.longitude
        }else{
            getLocation()
            locationCalllistener={
                lat = SoniApp.location!!.latitude
                long = SoniApp.location!!.longitude
            }
        }

        title = findViewById(R.id.tv_title)
        ivRight = findViewById(R.id.iv_right)
        ivBack = findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        ivBack.setOnClickListener { onBackPressed() }

        onClick()

        var screenId = -1


        binding.tvAddBtn.setOnClickListener {
            if (validate()) {
                if (isEdit) {
                    productData!!.title = binding.etTitle.text.toString()
                    productData!!.price = binding.etTrackerPrice.text.toString()
                    productData!!.description = binding.etTrackerDec.text.toString()
                    productData!!.pincode = binding.etSellerPincode.text.toString()
                    productData!!.username = binding.etSellerName.text.toString()
                    productData!!.phoneNumber = binding.etSellerNumber.text.toString()
                    if (sellerImage.isNotEmpty()) {
                        if (sellerImage[0].assetUrl.toString() != "null") {
                            if (!sellerImage[0].assetUrl.toString().startsWith("https")) {
                                productData!!.profileUrl = sellerImage[0].assetUrl

                            }
                        }
                    }
                }

                callApi()
            } else {
                showToast(error)
            }
        }

        binding.etSellerName.setText("${if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else ""} ${if (SoniApp.user!!.lastName != null) SoniApp.user!!.lastName else ""}")
        binding.etSellerNumber.setText(SoniApp.user!!.phoneNumber)
        binding.etSellerPincode.setText(SoniApp.user!!.pincode ?: "000000")


        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter(this, imageList) {
            callDeleteImageApi(it)

        }
//        val layoutManagerSeller =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        sellerImagesAdapter = ImagesAdapter(this, sellerImage){
//
//        }

        binding.rvImages.layoutManager = layoutManager
        binding.rvImages.adapter = imagesAdapter

        binding.rvSellerImage.visibility = View.GONE
        if (SoniApp.user!!.profileUrl.toString() != "null") {
            binding.tvSellerImage.text = SoniApp.user!!.profileUrl!!.split("/").last()
        }
//        binding.rvSellerImage.layoutManager = layoutManagerSeller
//        binding.rvSellerImage.adapter = sellerImagesAdapter

        if (intent.extras != null) {
            screenId = 1
            productData =
                intent.extras!!.getSerializable(Const.IntentKey.ProductData) as ProductData
            dropDown1Click()
            title.text = getString(R.string.edit_product)
            isEdit = true
            binding.rvImages.visibility = View.VISIBLE
            if(isEdit){
//                binding.llUploadImg.visibility=View.GONE
                imagesAdapter.allowAdd=true
                imagesAdapter.notifyDataSetChanged()
//                binding.llUploadImg.visibility=View.GONE
                binding.llSellerUploadImg.visibility=View.GONE
            }
            setData()
        }
        if (screenId == 1) {


        } else {
            title.text = getString(R.string.add_product)
            binding.tvAddBtn.text = getString(R.string.add)

            binding.rvImages.visibility = View.GONE
        }
        ActivityCompat.requestPermissions(
            this,
            SoniApp.REQUIRED_PERMISSIONS,
            SoniApp.REQUEST_CODE_PERMISSIONS
        )
    }


    fun imageChooser() {
        var sizeLimit = 5
        sizeLimit -= imageList.size
        val config = ImagePickerConfig {
            mode = ImagePickerMode.MULTIPLE
            limit = sizeLimit
            isShowCamera = false
            theme = R.style.ImagePickerTheme

        }
        binding.rvImages.visibility = View.VISIBLE
        launcher_roomImage.launch(config)
    }

    fun imageChooserSeller() {
        val config = ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
            limit = 1
            isShowCamera = false
            theme = R.style.ImagePickerTheme

        }
        binding.rvImages.visibility = View.VISIBLE
        launcher_SellerImage.launch(config)

    }

    private fun onClick() {

        binding.llDropDown.setOnClickListener {
            if (!isEdit) {
                if (!isDropDownOpen) {
                    binding.llDropDownOpen.visibility = View.VISIBLE

                    binding.llDetails.visibility = View.GONE
                    binding.ivArrowIcon.rotation = 180.0f
                    isDropDownOpen = true
                } else {
                    isDropDownOpen = false
                    binding.ivArrowIcon.rotation = 0.0f
                    binding.llDropDownOpen.visibility = View.GONE
                    when (ProductType) {
                        1 -> {
                            binding.llDrop1.performClick()
                        }
                        2 -> {
                            binding.llDrop3.performClick()
                        }
                        3 -> {
                            binding.llDrop2.performClick()
                        }
                    }


                }
            }
        }

        binding.llDrop1.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation = 0.0f
            dropDown1Click()
        }

        binding.llDrop2.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation = 0.0f
            binding.llDetails.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE

            binding.ivDropDownIconMain.setImageResource(R.drawable.agri_tools_icon)
            binding.txtDropDownMain.text = this.getString(R.string.agri_tools)
            if (ProductType != 2) {
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()
            }
            ProductType = 2

        }

        binding.llDrop3.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation = 0.0f
            binding.llDetails.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE

            binding.ivDropDownIconMain.setImageResource(R.drawable.agri_products_icon)
            binding.txtDropDownMain.text = this.getString(R.string.agri_products)
            if (ProductType != 3) {
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()

            }
            ProductType = 3

        }


        binding.llCamera.setOnClickListener {
            if (SoniApp.allPermissionsGranted(this)) {
                var sizeLimit = 5
                sizeLimit -= imageList.size
                if(sizeLimit>0) {
                    isForSeller = false
                    var cal=Calendar.getInstance()
                    val timeStamp: String = "${cal.time.time}.jpg"
                    val imageFileName = "$timeStamp.jpg"

                    var  values = ContentValues()

                    values.put(MediaStore.Images.Media.TITLE, imageFileName)

                    imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                    startActivityForResult(intent, CAMERA_REQUEST_CODE)

                }else{
                    showToast(getString(R.string.max_photos))
                }
            }else{
                if (SoniApp.allPermissionsDenied(this)){
                    showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        this,
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }

        }
        binding.txtBrowse.setOnClickListener {

            if (SoniApp.allPermissionsGranted(this)) {
                var sizeLimit = 5
                sizeLimit -= imageList.size
                if(sizeLimit>0) {
                    imageChooser()

                }else{
                    showToast(getString(R.string.max_photos))
                }
            }else{
                if (SoniApp.allPermissionsDenied(this)){
                    showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        this,
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }
        }

        binding.llSellerImage.setOnClickListener {
            if (SoniApp.allPermissionsGranted(this)) {
                isForSeller = true
                var cal = Calendar.getInstance()
                val timeStamp: String = "${cal.time.time}.jpg"
                val imageFileName = "$timeStamp.jpg"

                var values = ContentValues()

                values.put(MediaStore.Images.Media.TITLE, imageFileName)

                imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                if (SoniApp.allPermissionsDenied(this)){
                    showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        this,
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }
        }

        binding.txtSellerBrowse.setOnClickListener {
            if (SoniApp.allPermissionsGranted(this)) {
                imageChooserSeller()
            }else{
                if (SoniApp.allPermissionsDenied(this)){
                    showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        this,
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }
        }

        binding.etTitle.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llTitle, b)
        }
        binding.etTrackerPrice.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llTrackerPrice, b)
        }
        binding.etTrackerDec.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llTrackerDec, b)
        }
        binding.etSellerName.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llSellerName, b)
        }
        binding.etSellerNumber.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llSellerNumber, b)
        }
        binding.etSellerPincode.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llSellerPincode, b)
        }

        binding.llLocation.setOnClickListener {
            openMap()
        }
    }

    var launcher_roomImage = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)
            val imageModel = Propertyassets()
            imageModel.assetUrl = image.path
            if (productData != null) {
                productData!!.productassets.add(imageModel)
            }
            imageList.add(imageModel)
            imagesAdapter.notifyDataSetChanged()
        }

    }

    var launcher_SellerImage = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)
            sellerImage.clear()
            val imageModel = Propertyassets()
            imageModel.assetUrl = image.path
            sellerImage.add(imageModel)
            binding.tvSellerImage.text = image.path.split("/").last()
        }
    }

    private fun dropDown1Click() {
        binding.llDropDownOpen.visibility = View.GONE
        binding.llDetails.visibility = View.VISIBLE
        binding.ivDropDownIconMain.setImageResource(R.drawable.animal_icon)
        binding.txtDropDownMain.text = this.getString(R.string.animal)
        binding.llRvView.visibility = View.VISIBLE

        if (ProductType != 1) {
            imageList.clear()
            imagesAdapter.notifyDataSetChanged()

        }
        ProductType = 1
    }

    private fun validate(): Boolean {


        when (ProductType) {
            1 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                } else if (binding.etTrackerPrice.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_price)
                    return false
                }
//                else if (binding.etTrackerDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if (imageList.isEmpty()) {
                    error = getString(R.string.add_images)
                    return false
                }

            }
            2 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                } else if (binding.etTrackerPrice.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_price)
                    return false
                }
//                else if (binding.etTrackerDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if (imageList.isEmpty()) {
                    error = getString(R.string.add_images)
                    return false
                }

            }
            3 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                } else if (binding.etTrackerPrice.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_price)
                    return false
                }
//                else if (binding.etTrackerDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if (imageList.isEmpty()) {
                    error = getString(R.string.add_images)
                    return false
                }

            }

            else -> {
                error = getString(R.string.add_product_type)
                return false
            }
        }

        if (binding.etSellerName.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_seller_name)
            return false
        } else
            if (binding.etSellerNumber.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_seller_number)
            return false
        } else if (binding.etSellerNumber.text.toString().trim().length < 10) {
            error = getString(R.string.invalid_seller_number)
            return false
        } else if (binding.etSellerPincode.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_seller_pincode)
            return false
        } else if (binding.etSellerPincode.text.toString().trim().length < 6) {
            error = getString(R.string.invalid_seller_pincode)
            return false
        }
//            else if (sellerImage.isEmpty()) {
//            error = getString(R.string.add_seller_image)
//            return false
//        }
            else {
            return true

        }


    }

    private fun setData() {
        when (productData!!.categoryId) {
            "1" -> {
                binding.llDrop1.performClick()
            }
            "2" -> {
                binding.llDrop2.performClick()


            }
            "3" -> {
                binding.llDrop3.performClick()

            }
            else -> {

            }
        }
        imageList.clear()
        if(productData!!.productassets!!.size>5){
           productData!!.productassets!!.subList(SoniApp.productData!!.productassets!!.size-5,productData!!.productassets!!.size).map {
                imageList.add(it)
            }

        }else{
            imageList.addAll(productData!!.productassets)

        }
        imagesAdapter.notifyDataSetChanged()

        imgCount =0
        binding.etTitle.setText(productData!!.title)
        binding.etTrackerPrice.setText(productData!!.price.toString().split(".")[0])
        binding.etTrackerDec.setText(productData!!.description)
        binding.etSellerName.setText(productData!!.username)
        binding.etSellerNumber.setText(productData!!.phoneNumber)

        binding.rvImages.visibility = View.VISIBLE
        if(isEdit) {
            sellerImage.add(Propertyassets(-1, assetUrl = productData!!.profileUrl))
        }else{
            sellerImage.add(Propertyassets(-1, assetUrl = SoniApp.user!!.profileUrl))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE ) {
                //Image Uri will not be null for RESULT_OK


                // Use Uri object instead of File to avoid storage permissions
                if (isForSeller){
                    var path=getRealPathFromURI(imageUri)?:""
                    Log.d("result",File(path).length().toString())

                    GlobalScope.launch(Dispatchers.Unconfined) {

                        sellerImage.clear()
                        val imageModel = Propertyassets()
                        imageModel.assetUrl = compressImage(File(path)).path!!
                        this@AddEditProductsActivity.runOnUiThread(java.lang.Runnable {
                            sellerImage.add(imageModel)

                            binding.tvSellerImage.text = imageModel!!.assetUrl!!.split("/").last()
                        })

                    }

                }
                else{
                    var path=getRealPathFromURI(imageUri)?:""
                    Log.d("result",File(path).length().toString())

                    GlobalScope.launch(Dispatchers.Unconfined) {

                        val imageModel = Propertyassets()
                        imageModel.assetUrl = compressImage(File(path)).path!!


                        this@AddEditProductsActivity.runOnUiThread(java.lang.Runnable {
                            if (productData != null) {
                                productData!!.productassets.add(imageModel)
                            }
                            binding.rvImages.visibility=View.VISIBLE
                            imageList.add(imageModel)
                            imagesAdapter.notifyDataSetChanged()
                        })}



                }

            }
        } catch (e: java.lang.Exception) {
//            showToast("Something went wrong")

        }
    }


    fun callApi() {

        if (isInternetAvailable(this)) {
            binding.tvAddBtn.visibility = View.GONE
            binding.txtShimmer.visibility = View.VISIBLE


            val mHashMap: HashMap<String, RequestBody> = HashMap()
            val tractor_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)

            if (productData != null) {
                mHashMap.put(
                    "edit_id",
                    createPartFromString(productData!!.id.toString())
                )
            }
            mHashMap.put(
                "Latitude",
                createPartFromString(lat.toString())
            )
            mHashMap.put(
                "Longitude",
                createPartFromString(
                    long.toString()
                )
            )
            mHashMap.put(
                "category_id",
                createPartFromString(ProductType.toString())
            )
            mHashMap.put(
                "title",
                createPartFromString(
                    binding.etTitle.text.toString().trim()
                )
            )

            mHashMap.put(
                "price",
                createPartFromString(
                    binding.etTrackerPrice.text.toString().trim()
                )
            )
            mHashMap.put(
                "description",
                createPartFromString(
                    if(binding.etTrackerDec.text.toString().trim().isEmpty())"-" else binding.etTrackerDec.text.toString().trim()
                )
            )
            mHashMap.put(
                "u_name",
                createPartFromString(
                    binding.etSellerName.text.toString().trim()
                )
            )
            mHashMap.put(
                "u_mobile",
                createPartFromString(
                    binding.etSellerNumber.text.toString().trim()
                )
            )
            mHashMap.put(
                "u_pincode",
                createPartFromString(
                    binding.etSellerPincode.text.toString().trim()
                )
            )

            imageList.forEachIndexed { i, it ->
                if (!it.assetUrl!!.startsWith("http", true)) {
                    var part = createPartFromFile(
                        it.assetUrl!!,
                        "product_assets[]"
                    )
                    tractor_assets[i] = part
                }
            }
            var profile_image: MultipartBody.Part? = null
            if (sellerImage.isNotEmpty()) {
                if (sellerImage[0].assetUrl.toString() != "null"&& !sellerImage[0].assetUrl.toString().isEmpty()) {
                    if (!sellerImage[0].assetUrl.toString().startsWith("https")) {
                        profile_image = createPartFromFile(
                            sellerImage[0].assetUrl!!,
                            "profile_image"
                        )
                    }
                }
            }
            SoniApp.mApiCall?.postAddEditProduct(tractor_assets, profile_image, mHashMap)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.tvAddBtn.visibility = View.VISIBLE
                        binding.txtShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    showToast(responseLogin.message?:"")
                                    binding.tvAddBtn.visibility = View.VISIBLE
                                    binding.txtShimmer.visibility = View.GONE
                                 if(isEdit){
                                     SoniApp.productData = productData
                                     SoniApp.productData!!.productassets=imageList
                                 }
                                    onBackPressed()
                                }
                            }
                        }
                        binding.tvAddBtn.visibility = View.VISIBLE
                        binding.txtShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.tvAddBtn.visibility = View.VISIBLE
            binding.txtShimmer.visibility = View.GONE

        }

    }

    fun callDeleteImageApi(id: String) {

        if (isInternetAvailable(this)) {

            val mHashMap: HashMap<String, String> = HashMap()
            mHashMap["id"] = id
            mHashMap["type "]="2"
            SoniApp.productData!!.productassets=imageList
            SoniApp.mApiCall?.postDeleteImage(mHashMap)
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
                                Log.d("Image Delete", responseLogin.message!!)

                            }
                        }
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))


        }

    }

    override fun onPause() {
        hideKeyboard(binding.root)
        super.onPause()
    }

    fun openMap(){
        var intent=Intent(this@AddEditProductsActivity,MapActivity::class.java)
        startActivity(intent)
    }

    override fun onUpdate(latitude: Number, longitude: Number) {
        lat=latitude
        long=longitude
    }
}