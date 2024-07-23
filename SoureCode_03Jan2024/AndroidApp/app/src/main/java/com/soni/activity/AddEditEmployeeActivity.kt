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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityAddEditEmployeeBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.EmployeeData
import com.soni.services.web.models.Propertyassets
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
import java.util.*
import kotlin.collections.ArrayList

class AddEditEmployeeActivity : BaseActivity() {
    lateinit var binding:ActivityAddEditEmployeeBinding
    var isEdit:Boolean=false
    var RC_GALLERY_PERM = 100
    var imagePath=""
    var employee: EmployeeData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding =ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isEdit=intent.getBooleanExtra("isEdit",false)
        if (isEdit){
            employee=intent.getSerializableExtra(Const.IntentKey.Employee) as EmployeeData
            setData()
        }
        init()
    }
   fun setData(){

       if(employee!!.department.toString()!="null" ) {
           binding.etEmployeeDept.setText(employee!!.department.toString())
       }else{
           binding.etEmployeeDept.setText("")
       }
       if(employee!!.employeeId.toString()!="null" ) {
           binding.etEmployeeId.setText(employee!!.employeeId.toString())
       }else{
           binding.etEmployeeId.setText("")
       }
       if(employee!!.employeeEmail.toString()!="null" ) {
           binding.etEmployeeEmail.setText(employee!!.employeeEmail.toString())
       }else{
           binding.etEmployeeEmail.setText("")
       }
       if(employee!!.mobileNo.toString()!="null" ) {
           binding.etEmployeeMobile.setText(employee!!.mobileNo.toString())
       }else{
           binding.etEmployeeMobile.setText("")
       }

       binding.etEmployeeName.setText(employee!!.employeeName.toString())

       if (employee!!.profilePicture.toString()!="null") {
           imagePath= employee!!.profilePicture!!
           Glide.with(this)
               .load(employee!!.profilePicture!!)
               .placeholder(R.drawable.logo)
               .error(R.drawable.logo)
               .into(binding.ivEmployeeImg)
       }


   }
    private fun init(){
        ivBack= findViewById(R.id.iv_back)
        ivRight= findViewById(R.id.iv_right)
        title= findViewById(R.id.tv_title)
        if (isEdit){
            title.setText(getString(R.string.edit_employee))
            binding.txtAddBtn.setText(getString(R.string.update))
        }
        else{
            title.setText(getString(R.string.add_employee))
        }
        ivRight.visibility= View.INVISIBLE
        ivBack.setOnClickListener {
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


        binding.etEmployeeName.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llEmployeeName, b)
        }
        binding.etEmployeeId.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llEmployeeId, b)
        }
        binding.etEmployeeEmail.setOnFocusChangeListener { view, b ->
           onFocusChange(binding.llEmployeeEmail, b)
        }
        binding.etEmployeeMobile.setOnFocusChangeListener { view, b ->
           onFocusChange(binding.llEmployeeMobile, b)
        }
        binding.etEmployeeDept.setOnFocusChangeListener { view, b ->
           onFocusChange(binding.llEmployeeDept, b)
        }

        binding.ivCamera.setOnClickListener {
            if (SoniApp.allPermissionsGranted(this)) {
                showYesNOsDialog(getString(R.string.app_name),
                    getString(R.string.select_option),
                    getString(
                        R.string.Camera
                    ),
                    getString(R.string.gallery),
                    {
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
                    },
                    {
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
    }

    fun imageChooser() {
        val config = ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
            isShowCamera = false
            theme = R.style.ImagePickerTheme
        }
        launcher_roomImage?.launch(config)
    }
    var launcher_roomImage = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)

            imagePath=image.path
            Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.ivEmployeeImg)



        }
    }


    private fun validate(): Boolean {


//        if (imagePath.isEmpty()) {
//            error = getString(R.string.add_employee_picture)
//            return false
//        }else
            if (binding.etEmployeeName.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_employee_name)
            return false
        }
//            else if (binding.etEmployeeId.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_employee_id)
//            return false
//        } else if (binding.etEmployeeEmail.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_email_address)
//            return false
//        }
            else if (binding.etEmployeeEmail.text.toString().trim().isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmployeeEmail.text.toString().trim()
            ).matches()) {
            error = getString(R.string.invalid_email)
            return false

        }
//            else if (binding.etEmployeeMobile.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_mobile)
//            return false
//        }
            else if (binding.etEmployeeMobile.text.toString().trim().isNotEmpty() && binding.etEmployeeMobile.text.toString().trim().length < 10) {
            error = getString(R.string.invalid_phone_number)
            return false
        }
//            else if (binding.etEmployeeDept.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_employee_dept)
//            return false
//        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK  && requestCode == CAMERA_REQUEST_CODE ) {
                //Image Uri will not be null for RESULT_OK


                // Use Uri object instead of File to avoid storage permissions
                var path=getRealPathFromURI(imageUri)?:""
                Log.d("result",File(path).length().toString())

                GlobalScope.launch(Dispatchers.Unconfined) {
                    imagePath=compressImage(File(path)).path!!
                    this@AddEditEmployeeActivity.runOnUiThread(java.lang.Runnable {
                        Glide.with(this@AddEditEmployeeActivity)
                            .load(imagePath)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(binding.ivEmployeeImg)
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
            if (isEdit){
                mHashMap.put(
                    "edit_id",
                    createPartFromString(employee!!.id!!.toString())
                )
            }

            mHashMap.put(
                "employee_name",
                createPartFromString(
                    binding.etEmployeeName.text.toString().trim()
                )
            )
            mHashMap.put(
                "employee_id",
                createPartFromString(
                    binding.etEmployeeId.text.toString().trim()
                )
            )
            mHashMap.put(
                "mobile_no",
                createPartFromString(
                    binding.etEmployeeMobile.text.toString().trim()
                )
            )
            mHashMap.put(
                "department",
                createPartFromString(
                    binding.etEmployeeDept.text.toString().trim()
                )
            )
            mHashMap.put(
                "employee_email",
                createPartFromString(
                    binding.etEmployeeEmail.text.toString().trim()
                )
            )
           
            var part: MultipartBody.Part? = null
            if (imagePath.isNotEmpty() && (!imagePath.contains("https") && !imagePath.contains("http")) ) {
                part = createPartFromFile(
                    imagePath,
                    "profile_picture"
                )
            }



            SoniApp.mApiCall?.postAddEditEmployee(part,mHashMap)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                       showToast(getString(R.string.api_error))
                        binding.txtAddBtn.visibility=View.VISIBLE
                        binding.txtShimmer.visibility=View.GONE
                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if(responseLogin.status!!){
                                   showToast(responseLogin.message!!)
                                   finish()
                                }
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
}