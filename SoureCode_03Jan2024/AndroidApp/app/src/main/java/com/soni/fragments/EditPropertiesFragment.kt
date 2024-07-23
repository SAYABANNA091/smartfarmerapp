package com.soni.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.soni.Preference.StoreUserData
import com.soni.Preference.storeLoginToken
import com.soni.Preference.storeUserID
import com.soni.R
import com.soni.SoniApp
import com.soni.SoniApp.Companion.REQUEST_CODE_PERMISSIONS
import com.soni.SoniApp.Companion.REQUIRED_PERMISSIONS
import com.soni.SoniApp.Companion.allPermissionsGranted
import com.soni.activity.BaseActivity
import com.soni.activity.HomeActivity
import com.soni.activity.MapActivity
import com.soni.adapter.ImagesAdapter
import com.soni.databinding.FragmentEditPropertiesBinding
import com.soni.model.ImageModel
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.PropertyModel
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
import retrofit2.http.Part
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class EditPropertiesFragment : BaseFragment() ,MapCallBack{
    lateinit var binding: FragmentEditPropertiesBinding
    var imageList = ArrayList<Propertyassets>()
    lateinit var imagesAdapter: ImagesAdapter
    var isDropDownOpen = false
    var propertyID: String = ""
    var RC_GALLERY_PERM = 100
    var propertyType: Int = -1
    var error = ""
    var imgCount =5
    var isEdit=false
    lateinit var propertyModel:PropertyModel
    var lat:Number? = null
    var long:Number? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPropertiesBinding.inflate(layoutInflater)
        SoniApp.mapCallbacks.add(this@EditPropertiesFragment)
        return binding.root

        //  return inflater.inflate(R.layout.fragment_edit_properties, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()


    }

    override fun onDestroy() {
        SoniApp.mapCallbacks.remove(this@EditPropertiesFragment)
        super.onDestroy()
    }
    private fun init() {
        if(SoniApp.location!=null) {
            lat = SoniApp.location!!.latitude
            long = SoniApp.location!!.longitude
        }else{
            HomeActivity.activity!!.getLocation()
        }
        tvTitle = requireView().findViewById(R.id.tv_title)
        ivRight = requireView().findViewById(R.id.iv_right)
        ivBack = requireView().findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        ivBack.setOnClickListener { (requireActivity() as HomeActivity).onBackPressed() }
        imagesAdapter = ImagesAdapter(requireContext(),  imageList,){
            callDeleteImageApi(it)
        }
        onClick()

        val bundle = arguments
        val screenId = bundle!!.getInt("screenType")

        propertyID = bundle!!.getString(Const.IntentKey.PropertyID, "")

        if (screenId == 1) {
            propertyModel= bundle!!.getSerializable(Const.IntentKey.Property) as PropertyModel

            dropDown1Click()
            tvTitle.text = getString(R.string.Edit_Properties)
            isEdit=true
            propertyType=propertyModel.propertyTypeId!!
            propertyID=propertyModel.id!!.toString()
//            binding.llUploadImg.visibility=View.GONE
            setData()

        } else {

            tvTitle.text = getString(R.string.Add_Properties)
            binding.tvAddBtn.text = getString(R.string.add)
        }

        binding.tvAddBtn.setOnClickListener {
            if (validate()){
                callApi()
            }else{
                (requireActivity() as HomeActivity).showToast(error)
            }
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter(requireContext(), imageList){
            callDeleteImageApi(it)
        }
        binding.rvImages.layoutManager = layoutManager
        binding.rvImages.adapter = imagesAdapter


        binding.llTractorLocation.setOnClickListener {
            openMap()
        }
        binding.llFarmLocation.setOnClickListener {
            openMap()
        }
        binding.llJcbLocation.setOnClickListener {
            openMap()
        }
        binding.llNurseryLocation.setOnClickListener {
            openMap()
        }
        binding.llVermicompostLocation.setOnClickListener {
            openMap()
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            REQUIRED_PERMISSIONS,
            SoniApp.REQUEST_CODE_PERMISSIONS
        )

    }

    fun openMap(){
        var intent=Intent(requireActivity(),MapActivity::class.java)
        requireActivity().startActivity(intent)
    }

    fun imageChooser() {
        var sizeLimit = 5
        sizeLimit = sizeLimit - imageList.size
        val config = ImagePickerConfig {
            mode = ImagePickerMode.MULTIPLE
            limit = sizeLimit
            isShowCamera = false
            theme = R.style.ImagePickerTheme
        }
        launcher_roomImage?.launch(config)
    }

    private fun onClick() {

        binding.llDropDown.setOnClickListener {
            if(!isEdit) {
                if (!isDropDownOpen) {
                    binding.llDropDownOpen.visibility = View.VISIBLE

                    binding.llTractor.visibility = View.GONE
                    binding.llFarm.visibility = View.GONE
                    binding.llJCB.visibility = View.GONE
                    binding.llNursery.visibility = View.GONE
                    binding.llVermicompost.visibility = View.GONE
                    binding.llRvView.visibility = View.GONE
                    binding.ivArrowIcon.rotation=180.0f
                    isDropDownOpen = true
                } else {
                    isDropDownOpen = false
                    binding.ivArrowIcon.rotation=0.0f
                    binding.llDropDownOpen.visibility = View.GONE
                    when (propertyType){
                        1-> {
                           binding.llDrop1.performClick()
                        }
                        2-> {
                            binding.llDrop3.performClick()
                            }
                        3-> {
                            binding.llDrop2.performClick()
                           }
                        4-> {
                            binding.llDrop4.performClick()
                           }
                        5-> {
                            binding.llDrop5.performClick()
                           }

                    }


                }
            }
        }


        binding.llDrop1.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation=0.0f
            dropDown1Click()
        }

        binding.llDrop2.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation=0.0f
            binding.llRvView.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE
            binding.llTractor.visibility = View.GONE
            binding.llFarm.visibility = View.VISIBLE
            binding.llJCB.visibility = View.GONE
            binding.llNursery.visibility = View.GONE
            binding.llVermicompost.visibility = View.GONE

            binding.ivDropDownIconMain.setImageResource(R.drawable.tree)
            binding.txtDropDownMain.text = requireContext().getString(R.string.farm)
            if(propertyType!=3){
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()
            }
            propertyType = 3

        }

        binding.llDrop3.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation=0.0f
            binding.llRvView.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE
            binding.llTractor.visibility = View.GONE
            binding.llFarm.visibility = View.GONE
            binding.llJCB.visibility = View.VISIBLE
            binding.llNursery.visibility = View.GONE
            binding.llVermicompost.visibility = View.GONE

            binding.ivDropDownIconMain.setImageResource(R.drawable.jcb_icon)
            binding.txtDropDownMain.text = requireContext().getString(R.string.jcb)
            if(propertyType!=2){
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()

            }
            propertyType = 2

        }

        binding.llDrop4.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation=0.0f
            binding.llRvView.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE
            binding.llTractor.visibility = View.GONE
            binding.llFarm.visibility = View.GONE
            binding.llJCB.visibility = View.GONE
            binding.llNursery.visibility = View.VISIBLE
            binding.llVermicompost.visibility = View.GONE

            binding.ivDropDownIconMain.setImageResource(R.drawable.nursory_name_icon)
            binding.txtDropDownMain.text = requireContext().getString(R.string.nursery)
            if(propertyType!=4){
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()

            }
            propertyType = 4

        }

        binding.llDrop5.setOnClickListener {
            isDropDownOpen = false
            binding.ivArrowIcon.rotation=0.0f
            binding.llRvView.visibility = View.VISIBLE
            binding.llDropDownOpen.visibility = View.GONE
            binding.llTractor.visibility = View.GONE
            binding.llFarm.visibility = View.GONE
            binding.llJCB.visibility = View.GONE
            binding.llNursery.visibility = View.GONE
            binding.llVermicompost.visibility = View.VISIBLE

            binding.ivDropDownIconMain.setImageResource(R.drawable.vermicompost_icon)
            binding.txtDropDownMain.text = requireContext().getString(R.string.vermicompost)
            if(propertyType!=5){
                imageList.clear()
                imagesAdapter.notifyDataSetChanged()

            }
            propertyType = 5

        }


        binding.llCamera.setOnClickListener {

            if (SoniApp.allPermissionsGranted(requireActivity())) {
                var sizeLimit = 5
                sizeLimit -= imageList.size
                if (sizeLimit > 0) {
                    var cal = Calendar.getInstance()
                    val timeStamp: String = "${cal.time.time}.jpg"
                    val imageFileName = "$timeStamp.jpg"

                    var values = ContentValues()

                    values.put(MediaStore.Images.Media.TITLE, imageFileName)

                    (requireActivity() as BaseActivity).imageUri =
                        requireActivity().contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                        )

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        (requireActivity() as BaseActivity).imageUri
                    )

                    startActivityForResult(
                        intent,
                        (requireActivity() as BaseActivity).CAMERA_REQUEST_CODE
                    )
                } else {
                    (requireActivity() as HomeActivity).showToast(getString(R.string.max_photos))
                }
            } else {

                if (SoniApp.allPermissionsDenied(requireActivity())){
                    HomeActivity.activity!!.showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }
        }
        binding.txtBrowse.setOnClickListener {
            if (SoniApp.allPermissionsGranted(requireActivity())) {
                var sizeLimit = 5
                sizeLimit -= imageList.size
                if (sizeLimit > 0) {
                    imageChooser()
                } else {
                    (requireActivity() as HomeActivity).showToast(getString(R.string.max_photos))
                }
            } else {
                if (SoniApp.allPermissionsDenied(requireActivity())){
                    HomeActivity.activity!!.showSettingsDialog(
                        getString(R.string.gallery_and_camera_permissions_required),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        {HomeActivity.activity!!. openAppSettings(); },
                        { })
                }
                else{
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        SoniApp.REQUIRED_PERMISSIONS,
                        SoniApp.REQUEST_CODE_PERMISSIONS
                    )

                }
            }
        }

        binding.etTitle.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTitle, b)
        }
        binding.etTrackerName.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTrackerName, b)
        }
        binding.etTrackerHorsePower.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTrackerHorsePower, b)
        }
        binding.etTrackerPrice.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTrackerPrice, b)
        }
        binding.etTrackerDec.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTrackerDec, b)
        }
        binding.etTractorPincode.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llTractorPincode, b)
        }

        binding.etJCBName.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJCBName, b)
        }
        binding.etJCBPrice.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJCBPrice, b)
        }
        binding.etJCBHorsePower.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJCBHorsePower, b)
        }
        binding.etJCBVillage.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJCBVillage, b)
        }
        binding.etJCBDec.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJCBDec, b)
        }
        binding.etJcbPincode.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llJcbPincode, b)
        }


        binding.etFarmName.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmName, b)
        }
        binding.etFarmSurveyNumber.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmSurveyNumber, b)
        }
        binding.etFarmSize.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmSize, b)
        }
        binding.etFarmVillage.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmVillage, b)
        }
        binding.etFarmPincode.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmPincode, b)
        }
        binding.etFarmDec.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llFarmDec, b)
        }


        binding.etNurseryName.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llNurseryName, b)
        }
        binding.etNurseryVillage.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llNurseryVillage, b)
        }
        binding.etNurseryPincode.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llNurseryPincode, b)
        }
        binding.etNurseryDec.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llNurseryDec, b)
        }


        binding.etVermicompostName.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llVermicompostName, b)
        }
        binding.etVermicompostVillage.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llVermicompostVillage, b)
        }
        binding.etVermicompostPincode.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llVermicompostPincode, b)
        }
        binding.etVermicompostDec.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llVermicompostDec, b)
        }

    }
    var launcher_roomImage = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)

            val imageModel = Propertyassets()
            imageModel.assetUrl = image.path
            imageList.add(imageModel)
            imagesAdapter.notifyDataSetChanged()
        }
    }

    private fun dropDown1Click() {
        binding.llDropDownOpen.visibility = View.GONE
        binding.llTractor.visibility = View.VISIBLE
        binding.llFarm.visibility = View.GONE
        binding.llJCB.visibility = View.GONE
        binding.llNursery.visibility = View.GONE
        binding.llVermicompost.visibility = View.GONE

        binding.ivDropDownIconMain.setImageResource(R.drawable.tractor_icon)
        binding.txtDropDownMain.text = requireContext().getString(R.string.tractor)
        binding.llRvView.visibility = View.VISIBLE
        if(propertyType!=1){
            imageList.clear()
            imagesAdapter.notifyDataSetChanged()

        }
        propertyType = 1
    }

    private fun validate(): Boolean {


        when (propertyType) {
            1 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                }
//                else if (binding.etTrackerName.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_trackor_name)
//                    return false
//                }
//                    else if (binding.etTrackerHorsePower.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_horsepower)
//                    return false
//                } else if (binding.etTrackerVillge.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_property_village)
//                    return false
//                } else if (binding.etTractorPincode.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_propery_pincode)
//                    return false
//                } else if (binding.etTractorPincode.text.toString().trim().length<6) {
//                    error = getString(R.string.invalid_prperty_pincode)
//                    return false
//                }  else if (binding.etTrackerPrice.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_price)
//                    return false
//                } else if (binding.etTrackerDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if(imageList.isEmpty()){
                    error=getString(R.string.add_images)
                    return false
                }

            }
            2 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                }
//                else if (binding.etJCBName.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_jcb_name)
//                    return false
//                }
//                else if (binding.etJCBHorsePower.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_horsepower)
//                    return false
//                } else if (binding.etJCBVillage.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_property_village)
//                    return false
//                }  else if (binding.etJcbPincode.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_propery_pincode)
//                    return false
//                } else if (binding.etJcbPincode.text.toString().trim().length<6) {
//                    error = getString(R.string.invalid_prperty_pincode)
//                    return false
//                } else if (binding.etJCBPrice.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_price)
//                    return false
//                } else if (binding.etJCBDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if(imageList.isEmpty()){
                    error=getString(R.string.add_images)
                    return false
                }

            }
            3 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                }
//                else if (binding.etFarmName.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_farm_name)
//                    return false
//                }
//                else if (binding.etFarmSize.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_farm_size)
//                    return false
//                } else if (binding.etFarmSurveyNumber.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_farm_survey)
//                    return false
//                } else if (binding.etFarmVillage.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_property_village)
//                    return false
//                } else if (binding.etFarmPincode.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_propery_pincode)
//                    return false
//                } else if (binding.etFarmPincode.text.toString().trim().length<6) {
//                    error = getString(R.string.invalid_prperty_pincode)
//                    return false
//                } else if (binding.etFarmDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if(imageList.isEmpty()){
                    error=getString(R.string.add_images)
                    return false
                }

            }
            4 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                }
//                else  if (binding.etNurseryName.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_nursery_name)
//                    return false
//                }
//                else if (binding.etNurseryVillage.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_property_village)
//                    return false
//                } else if (binding.etNurseryPincode.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_propery_pincode)
//                    return false
//                } else if (binding.etNurseryPincode.text.toString().trim().length<6) {
//                    error = getString(R.string.invalid_prperty_pincode)
//                    return false
//                }  else if (binding.etNurseryDec.text.toString().trim().isEmpty()) {
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if(imageList.isEmpty()){
                    error=getString(R.string.add_images)
                    return false
                }

            }
            5 -> {
                if (binding.etTitle.text.toString().trim().isEmpty()) {
                    error = getString(R.string.add_title)
                    return false
                }
//                else if (binding.etVermicompostName.text.toString().trim().isEmpty()){
//                    error = getString(R.string.add_vermicompost_name)
//                    return false
//                }
//                else if(binding.etVermicompostVillage.text.toString().trim().isEmpty()){
//                    error = getString(R.string.add_property_village)
//                    return false
//                }
//                else if  (binding.etVermicompostPincode.text.toString().trim().isEmpty()){
//                    error = getString(R.string.add_propery_pincode)
//                    return false
//                }else if (binding.etVermicompostPincode.text.toString().trim().length<6) {
//                    error = getString(R.string.invalid_prperty_pincode)
//                    return false
//                }
//                else if(binding.etVermicompostDec.text.toString().trim().isEmpty()){
//                    error = getString(R.string.add_description)
//                    return false
//                }
                else if(imageList.isEmpty()){
                    error=getString(R.string.add_images)
                    return false
                }

            }
            else -> {
                error = getString(R.string.add_porperty_type)
                return false
            }
        }
        return true
    }
    private fun setData()  {
        imageList=propertyModel.propertyassets!!
        imgCount -= imageList.size
        when (propertyType) {
            1 -> {
                binding.etTitle.setText(if(propertyModel.tractorTitle=="-") "" else propertyModel.tractorTitle)
                binding.etTrackerName.setText(if(propertyModel.tractorName=="-") "" else propertyModel.tractorName)
                binding.etTrackerHorsePower.setText(if(propertyModel.tractorHoursePower==0) "" else propertyModel.tractorHoursePower.toString())
                binding.etTrackerVillge.setText(if(propertyModel.tractorVillageName=="-") "" else propertyModel.tractorVillageName)
                binding.etTrackerPrice.setText(if(propertyModel.tractorPricePerHour.toString().split(".")[0] =="0")"" else propertyModel.tractorPricePerHour.toString().split(".")[0])
                binding.etTrackerDec.setText(if(propertyModel.tractorDescription=="-") "" else propertyModel.tractorDescription )
                binding.etTractorPincode.setText(if(propertyModel.tractorPincode=="-") "" else propertyModel.tractorPincode)
                binding.llDrop1.performClick()
            }
            2 -> {
                binding.etTitle.setText(if(propertyModel.jcbTitle=="-") "" else propertyModel.jcbTitle)
                binding.etJCBName.setText(if(propertyModel.jcbName=="-") "" else propertyModel.jcbName)
                binding.etJCBHorsePower.setText(if(propertyModel.jcbHoursePower=="0") "" else propertyModel.jcbHoursePower.toString())
                binding.etJCBVillage.setText(if(propertyModel.jcbVillageName=="-") "" else propertyModel.jcbVillageName)
                binding.etJCBPrice.setText(if(propertyModel.jcbPricePerHour.toString().split(".")[0] =="0")"" else propertyModel.jcbPricePerHour.toString().split(".")[0])
                binding.etJCBDec.setText(if(propertyModel.jcbDescription=="-") "" else propertyModel.jcbDescription )
                binding.etJcbPincode.setText(if(propertyModel.jcbPincode=="-") "" else propertyModel.jcbPincode)
                binding.llDrop3.performClick()

            }
            3 -> {
                binding.etTitle.setText(if(propertyModel.farmTitle=="-") "" else propertyModel.farmTitle)
                binding.etFarmName.setText(if(propertyModel.farmName=="-") "" else propertyModel.farmName)
                binding.etFarmSize.setText(if(propertyModel.farmNoOfAcers=="-") "" else propertyModel.farmNoOfAcers.toString())
                binding.etFarmSurveyNumber.setText(if(propertyModel.farmSurveyNo=="-") "" else propertyModel.farmSurveyNo.toString())
                binding.etFarmVillage.setText(if(propertyModel.farmVillegeName=="-") "" else propertyModel.farmVillegeName.toString())
                binding.etFarmPincode.setText(if(propertyModel.farmPincode=="-") "" else propertyModel.farmPincode.toString())
                binding.etFarmDec.setText(if(propertyModel.farmDescription=="-") "" else propertyModel.farmDescription.toString())
                binding.llDrop2.performClick()
            }
            4 -> {
                binding.etTitle.setText(if(propertyModel.nurseryTitle=="-") "" else propertyModel.nurseryTitle)
                binding.etNurseryName.setText(if(propertyModel.nurseryName=="-") "" else propertyModel.nurseryName)
                binding.etNurseryVillage.setText(if(propertyModel.nurseryVillageName=="-") "" else propertyModel.nurseryVillageName)
                binding.etNurseryPincode.setText(if(propertyModel.nurseryPincode=="-") "" else propertyModel.nurseryPincode)
                binding.etNurseryDec.setText(if(propertyModel.nurseryDescription=="-") "" else propertyModel.nurseryDescription)
                binding.llDrop4.performClick()

            }
            5 -> {
                binding.etTitle.setText(if(propertyModel.vermicompostTitle=="-") "" else propertyModel.vermicompostTitle)
                binding.etVermicompostName.setText(if(propertyModel.vermicompostName=="-") "" else propertyModel.vermicompostName)
                binding.etVermicompostVillage.setText(if(propertyModel.vermicompostVillageName=="-") "" else propertyModel.vermicompostVillageName)
                binding.etVermicompostPincode.setText(if(propertyModel.vermicompostPincode=="-") "" else propertyModel.vermicompostPincode)
                binding.etVermicompostDec.setText(if(propertyModel.vermicompostDescription=="-") "" else propertyModel.vermicompostDescription)
                binding.llDrop5.performClick()


            }
            else -> {

            }
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                imagesAdapter.allowAdd=true
                imagesAdapter.notifyDataSetChanged()
            },
            1000 // value in milliseconds
        )


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK  && requestCode ==(requireActivity() as BaseActivity). CAMERA_REQUEST_CODE ) {
                //Image Uri will not be null for RESULT_OK


                // Use Uri object instead of File to avoid storage permissions
                var path=(requireActivity() as BaseActivity).getRealPathFromURI((requireActivity() as BaseActivity).imageUri)?:""
                Log.d("result",File(path).length().toString())

                GlobalScope.launch(Dispatchers.Unconfined) {

                    val imageModel = Propertyassets()
                    imageModel.assetUrl = (requireActivity() as BaseActivity).compressImage(File(path)).path!!
                    imageList.add(imageModel)
                    requireActivity().runOnUiThread(java.lang.Runnable {
                        imagesAdapter.notifyDataSetChanged()

                    })
                }



            }  else {

            }
        } catch (e: java.lang.Exception) {
//            showToast("Something went wrong")

        }
    }

    fun callApi() {

        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {
            binding.tvAddBtn.visibility=View.GONE
            binding.txtShimmer.visibility=View.VISIBLE

            val mHashMap: java.util.HashMap<String, RequestBody> = HashMap()
            val tractor_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)
            val jcb_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)
            val farm_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)
            val nursery_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)
            val vermicompost_assets = arrayOfNulls<MultipartBody.Part>(imageList.size)
            if (propertyID.isNotEmpty()) {
                mHashMap.put(
                    "edit_id",
                    (requireActivity() as BaseActivity).createPartFromString(propertyID)
                )
            }
            mHashMap.put(
                "property_type_id",
                (requireActivity() as BaseActivity).createPartFromString(propertyType.toString())
            )

            mHashMap.put(
                "Latitude",
                (requireActivity() as BaseActivity).createPartFromString(
                    lat.toString()
                )
            )
            mHashMap.put(
                "Longitude",
                (requireActivity() as BaseActivity).createPartFromString(
                    long.toString()
                )
            )

            when (propertyType) {
                1 -> {
                    mHashMap.put(
                        "tractor_title",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTitle.text.toString().trim().isEmpty()) "-" else binding.etTitle.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTrackerName.text.toString().trim().isEmpty()) "-" else binding.etTrackerName.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_hourse_power",
                        (requireActivity() as BaseActivity).createPartFromString(
                          if ( binding.etTrackerHorsePower.text.toString().trim().isEmpty())"0" else binding.etTrackerHorsePower.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_village_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etTrackerVillge.text.toString().trim().isEmpty())"-" else binding.etTrackerVillge.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_price_per_hour",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etTrackerPrice.text.toString().trim().isEmpty())"0.00" else binding.etTrackerPrice.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_description",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etTrackerDec.text.toString().trim().isEmpty())"-" else binding.etTrackerDec.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "tractor_pincode",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etTractorPincode.text.toString().trim().isEmpty())"-" else binding.etTractorPincode.text.toString().trim()
                        )
                    )
                    imageList.forEachIndexed { i, it ->
                        if(!it.assetUrl!!.startsWith("https",true)) {
                            var part = (requireActivity() as BaseActivity).createPartFromFile(
                                it.assetUrl!!,
                                "tractor_assets[]"
                            )
                            tractor_assets[i] = part
                        }}
                }
                2 -> {
                    mHashMap.put(
                        "jcb_title",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTitle.text.toString().trim().isEmpty()) "-" else binding.etTitle.text.toString().trim()

                        )
                    )
                    mHashMap.put(
                        "jcb_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJCBName.text.toString().trim().isEmpty())"-" else binding.etJCBName.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "jcb_hourse_power",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJCBHorsePower.text.toString().trim().isEmpty())"0" else binding.etJCBHorsePower.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "jcb_village_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJCBVillage.text.toString().trim().isEmpty())"-" else binding.etJCBVillage.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "jcb_price_per_hour",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJCBPrice.text.toString().trim().isEmpty())"0.00" else binding.etJCBPrice.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "jcb_description",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJCBDec.text.toString().trim().isEmpty())"-" else binding.etJCBDec.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "jcb_pincode",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etJcbPincode.text.toString().trim().isEmpty())"-" else binding.etJcbPincode.text.toString().trim()
                        )
                    )
                    imageList.forEachIndexed { i, it ->
                        if(!it.assetUrl!!.startsWith("https",true)) {
                        var part = (requireActivity() as BaseActivity).createPartFromFile(
                            it.assetUrl!!,
                            "jcb_assets[]"
                        )
                        jcb_assets[i] = part
                    }}

                }
                3 -> {
                    mHashMap.put(
                        "farm_title",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTitle.text.toString().trim().isEmpty()) "-" else binding.etTitle.text.toString().trim()

                        )
                    )
                    mHashMap.put(
                        "farm_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmName.text.toString().trim().isEmpty())"-" else binding.etFarmName.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "farm_no_of_acers",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmSize.text.toString().trim().isEmpty())"-" else binding.etFarmSize.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "farm_survey_no",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmSurveyNumber.text.toString().trim().isEmpty())"-" else binding.etFarmSurveyNumber.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "farm_villege_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmVillage.text.toString().trim().isEmpty())"-" else binding.etFarmVillage.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "farm_pincode",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmPincode.text.toString().trim().isEmpty())"-" else binding.etFarmPincode.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "farm_description",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etFarmDec.text.toString().trim().isEmpty())"-" else binding.etFarmDec.text.toString().trim()
                        )
                    )
                    imageList.forEachIndexed { i, it ->
                        if(!it.assetUrl!!.startsWith("https",true)) {
                        var part = (requireActivity() as BaseActivity).createPartFromFile(
                            it.assetUrl!!,
                            "farm_assets[]"
                        )
                        farm_assets[i] = part
                    }
                    }

                }
                4 -> {
                    mHashMap.put(
                        "nursery_title",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTitle.text.toString().trim().isEmpty()) "-" else binding.etTitle.text.toString().trim()

                        )
                    )
                    mHashMap.put(
                        "nursery_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etNurseryName.text.toString().trim().isEmpty())"-" else binding.etNurseryName.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "nursery_village_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etNurseryVillage.text.toString().trim().isEmpty())"-" else binding.etNurseryVillage.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "nursery_pincode",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etNurseryPincode.text.toString().trim().isEmpty())"-" else binding.etNurseryPincode.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "nursery_description",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etNurseryDec.text.toString().trim().isEmpty())"-" else binding.etNurseryDec.text.toString().trim()
                        )
                    )
                    imageList.forEachIndexed { i, it ->
                        if(!it.assetUrl!!.startsWith("https",true)) {
                        var part = (requireActivity() as BaseActivity).createPartFromFile(
                            it.assetUrl!!,
                            "nursery_assets[]"
                        )
                        nursery_assets[i] = part
                    }
                    }

                }
                5 -> {
                    mHashMap.put(
                        "vermicompost_title",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if(binding.etTitle.text.toString().trim().isEmpty()) "-" else binding.etTitle.text.toString().trim()

                        )
                    )
                    mHashMap.put(
                        "vermicompost_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etVermicompostName.text.toString().trim().isEmpty())"-" else binding.etVermicompostName.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "vermicompost_village_name",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etVermicompostVillage.text.toString().trim().isEmpty())"-" else binding.etVermicompostVillage.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "vermicompost_pincode",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etVermicompostPincode.text.toString().trim().isEmpty())"-" else binding.etVermicompostPincode.text.toString().trim()
                        )
                    )
                    mHashMap.put(
                        "vermicompost_description",
                        (requireActivity() as BaseActivity).createPartFromString(
                            if ( binding.etVermicompostDec.text.toString().trim().isEmpty())"-" else binding.etVermicompostDec.text.toString().trim()
                        )
                    )
                    imageList.forEachIndexed { i, it ->
                        if (!it.assetUrl!!.startsWith("https", true)) {
                            var part = (requireActivity() as BaseActivity).createPartFromFile(
                                it.assetUrl!!,
                                "vermicompost_assets[]"
                            )
                            vermicompost_assets[i] = part
                        }
                    }
                }

            }



            SoniApp.mApiCall?.postAddEditProperty(tractor_assets,jcb_assets,farm_assets,nursery_assets,vermicompost_assets,mHashMap)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.tvAddBtn.visibility=View.VISIBLE
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
                                    (requireActivity() as HomeActivity).showToast(responseLogin.message!!)
                                    binding.tvAddBtn.visibility=View.VISIBLE
                                    binding.txtShimmer.visibility=View.GONE
                                    (requireActivity() as HomeActivity).onBackPressed()
                                }
                            }
                        }
                        binding.tvAddBtn.visibility=View.VISIBLE
                        binding.txtShimmer.visibility=View.GONE
                    }


                })

        } else {
          (requireActivity() as HomeActivity).  showToast(getString(R.string.internet_error))
            binding.tvAddBtn.visibility=View.VISIBLE
            binding.txtShimmer.visibility=View.GONE

        }

    }

    fun callDeleteImageApi(id:String) {

        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

            val mHashMap: HashMap<String, String> = HashMap()
            mHashMap["id"]=id
            mHashMap["type"]="1"

            SoniApp.mApiCall?.postDeleteImage(mHashMap)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
//                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))

                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                Log.d("Image Delete",responseLogin.message!!)

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).  showToast(getString(R.string.internet_error))


        }

    }

    override fun onUpdate(latitude: Number, longitude: Number) {

         lat=latitude
         long=longitude
    }
}



//    val mHashMap: java.util.HashMap<String, RequestBody> = java.util.HashMap()
