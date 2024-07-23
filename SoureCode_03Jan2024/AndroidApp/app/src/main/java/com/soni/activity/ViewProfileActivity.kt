package com.soni.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.DistrictSpinnerAdapter
import com.soni.adapter.StateSpinnerAdapter
import com.soni.adapter.TalukaSpinnerAdapter
import com.soni.databinding.ActivityViewProfileBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.District
import com.soni.services.web.models.DistrictListResponse
import com.soni.services.web.models.GetUser
import com.soni.services.web.models.Propertyassets
import com.soni.services.web.models.State
import com.soni.services.web.models.StateListResponse
import com.soni.services.web.models.Taluka
import com.soni.services.web.models.TalukaListResponse
import com.soni.utils.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList

class ViewProfileActivity : BaseActivity() {
    lateinit var binding: ActivityViewProfileBinding
    var RC_GALLERY_PERM = 100
    var imagePath=""
    var districtList: ArrayList<District> = arrayListOf()
    var talukaList: ArrayList<Taluka> = arrayListOf()
    var stateList: ArrayList<State> = arrayListOf()
    val popup: Field = AppCompatSpinner::class.java.getDeclaredField("mPopup")
    lateinit var stateAdapter: StateSpinnerAdapter
    lateinit var districtAdapter: DistrictSpinnerAdapter
    lateinit var talukaAdapter: TalukaSpinnerAdapter
    var selectedState = 0
    var selectedDistrict = 0
    var selectedTaluka = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_view_profile)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        setData()
        ivBack= findViewById(R.id.iv_back)
        ivRight= findViewById(R.id.iv_right)
        title= findViewById(R.id.tv_title)

        title.text = getString(R.string.profile)
        ivBack.setOnClickListener {
            hideKeyboard(binding.root)
            finish()
        }

        binding.txtAddBtn.setOnClickListener {
            if (validate()){
                binding.txtShimmer.visibility=View.VISIBLE
                binding.txtAddBtn.visibility=View.GONE
                callApi()
            }else{
                showToast(error)
            }
        }

        ivRight.visibility = View.INVISIBLE
        binding.etUserName.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llName, b)
        }
        binding.etLastName.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llLastName, b)
        }
        binding.etEmailId.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llEmail, b)
        }

        binding.ivChooseImage.setOnClickListener {
            if (SoniApp.allPermissionsGranted(this)){
            showYesNOsDialog(getString(R.string.app_name),getString(R.string.select_option),getString(
                R.string.Camera),getString(R.string.gallery),
                {
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
                },{
                    imageChooser()
                })
        }else {
                ActivityCompat.requestPermissions(
                    this,
                    SoniApp.REQUIRED_PERMISSIONS,
                    SoniApp.REQUEST_CODE_PERMISSIONS
                )
            }
        }

        binding.llEmail.visibility=View.GONE

        setStateSpinner()
        setDistrictSpinner()
        setTalukaSpinner()
        callStateListApi()

    }

    fun setData(){
        if (!SoniApp.user!!.profileUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(Const.API.BaseUrl +SoniApp.user!!.profileUrl!!)
                .placeholder(R.drawable.logo)
                .error(R.drawable.user_icon)
                .into(binding.ivUserImage)
        }

        binding.tvUserName.text=
            if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else "" +" "+if(SoniApp.user!!.lastName != null)SoniApp.user!!.lastName!! else ""
        binding.etUserName.setText( "${if(SoniApp.user!!.firstName != null)SoniApp.user!!.firstName!! else ""}")
        if(SoniApp.user!!.email==null) {
            binding.tvUserEmail.text = ""
            binding.etEmailId.setText("")
        }else{
            binding.tvUserEmail.text = "${SoniApp.user!!.email!!}"
            binding.etEmailId.setText("${SoniApp.user!!.email!!}")
        }
        binding.etPhone.setText("${SoniApp.user!!.phoneNumber!!}")
        binding.etLastName.setText("${if(SoniApp.user!!.lastName != null)SoniApp.user!!.lastName!! else ""}")
        binding.etPincode.setText("${if(SoniApp.user!!.pincode!=null) SoniApp.user!!.pincode else ""}")
    }

    fun imageChooser() {
        if (SoniApp.allPermissionsGranted(this)) {

                val config = ImagePickerConfig {
                    mode = ImagePickerMode.SINGLE
                    isShowCamera = false
                    theme = R.style.ImagePickerTheme
                }
                launcher_roomImage?.launch(config)


        } else {
            ActivityCompat.requestPermissions(
                this,
                SoniApp.REQUIRED_PERMISSIONS,
                SoniApp.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    var launcher_roomImage = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)

            GlobalScope.launch(Dispatchers.Unconfined) {
                imagePath = compressImage(File(image.path)).path!!

                this@ViewProfileActivity.runOnUiThread(java.lang.Runnable {
                    Glide.with(this@ViewProfileActivity)
                        .load(imagePath)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.user_icon)
                        .into(binding.ivUserImage)
                })
            }


        }
    }

    private fun validate(): Boolean {


//        if (binding.etUserName.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_first_name)
//            return false
//        }else if (binding.etLastName.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_last_name)
//            return false
//        }else  if (binding.etEmailId.text.toString().trim()
//                .isNotEmpty()  && !android.util.Patterns.EMAIL_ADDRESS.matcher(
//                binding.etEmailId.text.toString().trim()
//            ).matches()) {
//            error = getString(R.string.invalid_email)
//            return false
//        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK  && requestCode == CAMERA_REQUEST_CODE ) {
                //Image Uri will not be null for RESULT_OK


                // Use Uri object instead of File to avoid storage permissions
                var path=getRealPathFromURI(imageUri)?:""
                Log.d("result",File(path).length().toString())

                GlobalScope.launch(Dispatchers.Unconfined) {
                    imagePath=compressImage(File(path)).path!!
                    this@ViewProfileActivity.runOnUiThread(java.lang.Runnable {
                        Glide.with(this@ViewProfileActivity)
                            .load(imagePath)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.user_icon)
                            .into(binding.ivUserImage)
                    })
                }



            }  else {

            }
        } catch (e: java.lang.Exception) {
//            showToast("Something went wrong")

        }
    }

    fun callApi() {

        if (isInternetAvailable(this)) {


            val mHashMap: java.util.HashMap<String, RequestBody> = java.util.HashMap()


            mHashMap.put(
                "first_name",
                createPartFromString(
                    binding.etUserName.text.toString().trim()
                )
            )

            mHashMap.put(
                "last_name",
                createPartFromString(
                    binding.etLastName.text.toString().trim()
                )
            )
            mHashMap.put(
                "phone_number",
                createPartFromString(
                    SoniApp.user!!.phoneNumber!!
                )
            )
//            mHashMap.put(
//                "email",
//                createPartFromString(
//                    binding.etEmailId.text.toString().trim()
//                )
//            )
            if(selectedState>0) {
                mHashMap.put(
                    "state",
                    createPartFromString(
                        binding.etState.text.toString()
                    )
                )
            }
            if(selectedDistrict>0) {
                mHashMap.put(
                    "district",
                    createPartFromString(
                        binding.etDisrict.text.toString()
                    )
                )
            }

            if(selectedTaluka>0) {
                mHashMap.put(
                    "taluka",
                    createPartFromString(
                        binding.etTaluka.text.toString()
                    )
                )
            }

            mHashMap.put(
                "pincode",
                createPartFromString(
                    binding.etPincode.text.toString().trim()
                )
            )


            var part: MultipartBody.Part? = null
            if (imagePath.isNotEmpty()) {
                part = createPartFromFile(
                    imagePath,
                    "image"
                )
            }



            SoniApp.mApiCall?.postEditUser(part,mHashMap)
                ?.enqueue(object : Callback<GetUser> {
                    override fun onFailure(call: Call<GetUser>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtAddBtn.visibility=View.VISIBLE
                        binding.txtShimmer.visibility=View.GONE
                    }

                    override fun onResponse(
                        call: Call<GetUser>,
                        response: Response<GetUser>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if(responseLogin.status!!){
                                    StoreUserData(responseLogin!!.data!!)
                                    SoniApp.user=responseLogin!!.data!!
                                    finish()
                                }
                                showToast(responseLogin.message?:"")
                            }

                        }
                        binding.txtAddBtn.visibility=View.VISIBLE
                        binding.txtShimmer.visibility=View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtAddBtn.visibility=View.VISIBLE
            binding.txtShimmer.visibility=View.GONE
        }

    }


    private fun callStateListApi(
    ) {
        if (isInternetAvailable(this)) {
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"


            SoniApp.mApiCall?.postGeneralStateList(param)
                ?.enqueue(object : Callback<StateListResponse> {
                    override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<StateListResponse>, response: Response<StateListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        stateList.add(
                                            State(
                                                id = -1, name = getString(R.string.state)
                                            )
                                        )
                                        stateList.addAll(responseLogin.data)
                                        stateAdapter.notifyDataSetChanged()
                                        if (SoniApp.user!!.state!=null) {
                                        for(i in 0 until  responseLogin.data.size) {

                                                if (responseLogin.data[i].name.equals(
                                                        SoniApp.user!!.state!!,
                                                        true
                                                    )
                                                ) {
                                                    binding.spState.setSelection(i + 1)

                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")

                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")

        }


    }

    private fun callDistrictListApi(
    ) {
        if (isInternetAvailable(this)) {
            binding.districtShimmer.visibility = View.VISIBLE
            binding.llDistrict.visibility = View.INVISIBLE
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"
            param["states_id"] = selectedState.toString()


            SoniApp.mApiCall?.postGeneralDistrictList(param)
                ?.enqueue(object : Callback<DistrictListResponse> {
                    override fun onFailure(call: Call<DistrictListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                        binding.districtShimmer.visibility = View.GONE
                        binding.llDistrict.visibility = View.VISIBLE
                    }

                    override fun onResponse(
                        call: Call<DistrictListResponse>, response: Response<DistrictListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        districtList.addAll(responseLogin.data)
                                        districtAdapter.notifyDataSetChanged()
                                        if (SoniApp.user!!.district!=null) {
                                        for(i in 0 until  responseLogin.data.size) {
                                            if(responseLogin.data[i].name.equals(SoniApp.user!!.district!!,true)) {
                                                binding.spDistrict.setSelection(i+1)
                                            }}
                                    }
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")

                        }
                        binding.districtShimmer.visibility = View.GONE
                        binding.llDistrict.visibility = View.VISIBLE
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.districtShimmer.visibility = View.GONE
            binding.llDistrict.visibility = View.VISIBLE
        }


    }

    private fun callTalukaListApi(
    ) {

        if (isInternetAvailable(this)) {
            binding.talukaShimmer.visibility = View.VISIBLE
            binding.llTaluka.visibility = View.INVISIBLE
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"
            param["districts_id"] = selectedDistrict.toString()


            SoniApp.mApiCall?.postGeneralTalukaList(param)
                ?.enqueue(object : Callback<TalukaListResponse> {
                    override fun onFailure(call: Call<TalukaListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<TalukaListResponse>, response: Response<TalukaListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        talukaList.addAll(responseLogin.data)
                                        talukaAdapter.notifyDataSetChanged()
                                        if (SoniApp.user!!.district!=null) {
                                            for (i in 0 until responseLogin.data.size) {
                                                if (responseLogin.data[i].name.equals(
                                                        SoniApp.user!!.taluka!!,
                                                        true
                                                    )
                                                ) {
                                                    binding.spTaluka.setSelection(i + 1)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                            binding.talukaShimmer.visibility = View.GONE
                            binding.llTaluka.visibility = View.VISIBLE

                        } else {
                            Log.d("State ERROR", "DATA Not Found")
                            binding.talukaShimmer.visibility = View.GONE
                            binding.llTaluka.visibility = View.VISIBLE
                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.talukaShimmer.visibility = View.GONE
            binding.llTaluka.visibility = View.VISIBLE
        }


    }

    fun setStateSpinner() {
        stateAdapter = StateSpinnerAdapter(
            this@ViewProfileActivity, stateList
        )
        binding.spState.adapter = stateAdapter
        binding.llState.setOnClickListener {
            binding.spState.performClick()
        }
        popup.isAccessible = true
        val popupWindow = popup[binding.spState] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {
                binding.etState.text = stateList[it].name!!
                selectedState = stateList[it].id!!
                districtList.clear()
                districtList.add(
                    District(
                        id = -1, name = getString(R.string.district)
                    )
                )
                districtAdapter.notifyDataSetChanged()
                if(selectedState!=-1) {
                    callDistrictListApi()
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun setDistrictSpinner() {
        districtAdapter = DistrictSpinnerAdapter(
            this@ViewProfileActivity, districtList
        )
        binding.spDistrict.adapter = districtAdapter
        binding.llDistrict.setOnClickListener {
            if (districtList.size > 1) {
                binding.spDistrict.performClick()
            } else {
                showToast(getString(R.string.please_select_state))
            }
        }
        val popupWindow = popup[binding.spDistrict] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {

                binding.etDisrict.text = districtList[it].name!!
                selectedDistrict = districtList[it].id!!
                talukaList.clear()
                talukaList.add(
                    Taluka(
                        id = -1, name = getString(R.string.taluka)
                    )
                )
                talukaAdapter.notifyDataSetChanged()
                if(selectedDistrict!=-1) {
                    callTalukaListApi()
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun setTalukaSpinner() {
        talukaAdapter = TalukaSpinnerAdapter(
            this@ViewProfileActivity, talukaList
        )
        binding.spTaluka.adapter = talukaAdapter
        binding.llTaluka.setOnClickListener {
            if (talukaList.size > 1) {
                binding.spTaluka.performClick()
            } else {
                showToast("Please select a district")
            }
        }
        val popupWindow = popup[binding.spTaluka] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spTaluka.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {
                binding.etTaluka.text = talukaList[it].name!!
                selectedTaluka = talukaList[it].id!!
//                districtList.clear()
//                districtList.add(
//                    District(
//                        id = -1, name = getString(R.string.market)
//                    )
//                )
//                callDistrictListApi()
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}