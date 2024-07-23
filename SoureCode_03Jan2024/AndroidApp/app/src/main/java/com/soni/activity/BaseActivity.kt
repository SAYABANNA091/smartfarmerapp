package com.soni.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.soni.R
import com.soni.SoniApp
import com.soni.SoniApp.Companion.fusedLocationClient
import com.soni.SoniApp.Companion.isFirstLocationRequest
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


open class BaseActivity : AppCompatActivity() {
    lateinit var ivBack: ImageView
    lateinit var ivRight: ImageView
    lateinit var title: TextView
    var error: String = ""
    var isPasswordVisible = false
    var isCPasswordVisible = false
    var isNCPasswordVisible = false
    val CAMERA_REQUEST_CODE = 1
    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_LOCATION = 199
    val REQUEST_ENABLE_GPS = 516
    var dialog: Dialog? = null
    var imageUri: Uri? = null

    var locationCalllistener: ((Location) -> Unit)? = null
    var REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.CAMERA,
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }.toTypedArray()


    fun allPermissionsGranted(baseContext: Activity) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    var REQUIRED_PERMISSIONS_LOC =
        mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ).toTypedArray()

    fun togglePassVisability(text: String, editText: EditText, imageView: ImageView) {
        val pass: String = text
        var typeface = ResourcesCompat.getFont(this, R.font.kumbh_sans_regular)
        if (isPasswordVisible) {

            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.typeface = typeface
            editText.setText(pass)
            editText.setSelection(pass.length)
            imageView.setImageResource(R.drawable.password_visible_icon)

        } else {
            imageView.setImageResource(R.drawable.password_not_visible_icon)
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.setTypeface(typeface)
            editText.setText(pass)
            editText.setSelection(pass.length)
        }
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleCPassVisability(text: String, editText: EditText, imageView: ImageView) {
        val pass: String = text
        var typeface = ResourcesCompat.getFont(this, R.font.kumbh_sans_regular)
        if (isCPasswordVisible) {

            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setText(pass)
            editText.setTypeface(typeface)
            editText.setSelection(pass.length)
            imageView.setImageResource(R.drawable.password_visible_icon)

        } else {
            imageView.setImageResource(R.drawable.password_not_visible_icon)
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.setText(pass)
            editText.setTypeface(typeface)
            editText.setSelection(pass.length)
        }
        isCPasswordVisible = !isCPasswordVisible
    }

    fun toggleNPassVisability(text: String, editText: EditText, imageView: ImageView) {
        val pass: String = text
        var typeface = ResourcesCompat.getFont(this, R.font.kumbh_sans_regular)
        if (isPasswordVisible) {

            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setText(pass)
            editText.setTypeface(typeface)
            editText.setSelection(pass.length)
            imageView.setImageResource(R.drawable.password_visible_icon)

        } else {
            imageView.setImageResource(R.drawable.password_not_visible_icon)
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.setText(pass)
            editText.setTypeface(typeface)
            editText.setSelection(pass.length)
        }
        isPasswordVisible = !isPasswordVisible
    }

    fun Date.ddMmYyyy(): String {
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(this)
    }

    fun Date.yyyyMMDD(): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(this)
    }

//    override fun attachBaseContext(newBase: Context?) {
//        if (newBase != null) {
//            super.attachBaseContext(LocaleHelper.setLocale(newBase, getCurrentLanguage()))
//        } else {
//            super.attachBaseContext(newBase)
//        }
//        LocaleHelper.setLocale(this, getCurrentLanguage())
//
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        Log.e(
//            "tagLanguage",
//            "BaseActivity onConfigurationChanged getCurrentLanguage()"
//        )
//    }

    fun isInternetAvailable(context: Context): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
//    fun showToast(message:String){
//        Toast.makeText(
//            this, message,
//            Toast.LENGTH_LONG
//        ).show()
//    }

    fun onFocusChange(view: View?, hasFocus: Boolean) {
        val background: Int = if (hasFocus) {
            R.drawable.edit_text_focus
        } else {
            R.drawable.simple_edittext
        }
        view!!.setBackgroundResource(background)
    }

    fun createPartFromString(descriptionString: String?): RequestBody {
        return if (descriptionString == null) ""
            .toRequestBody(MultipartBody.FORM) else descriptionString
            .toRequestBody(MultipartBody.FORM)
    }

    fun createPartFromFile(path: String, dec: String): MultipartBody.Part {
        var file = File(path)

        val requestFile: RequestBody =
            file.let { it.asRequestBody("multipart/form-data".toMediaTypeOrNull()) }
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData(dec, file.name, requestFile)
        val fullName: RequestBody =
            file.name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return body
    }

    fun showToast(message: String) {
        if (message != null && !TextUtils.isEmpty(message) && !message.equals(
                "null",
                ignoreCase = true
            )
        ) {

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = inflater.inflate(
                R.layout.layout_toast, findViewById<ViewGroup>(R.id.toast_layout_root)
            )


            layout.findViewById<TextView>(R.id.text_toast).text = message

            val toast = Toast(this)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }
    }


    private val requestPermissionLauncher = this.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            getLocation()

        } else {
            // TODO: Inform user that that your app will not show notifications.
            getLocation()
        }

    }

    fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
getLocation()
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                //       display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this@BaseActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient!!.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->

                        Log.d(
                            "Location error",
                            "Location error " + connectionResult.errorCode
                        )
                        showSettingsDialog(
                            getString(R.string.location_permission_request),
                            getString(R.string.open),
                            getString(R.string.cancel),
                            { openAppSettings() },
                            { finish() }
                        )

                }.build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result =
                LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient!!,
                    builder.build()
                )
            result.setResultCallback { result ->
                val status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            this@BaseActivity,
                            REQUEST_LOCATION
                        )
//                        finish()
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }else{
            showSettingsDialog(
                getString(R.string.location_permission_request),
                getString(R.string.open),
                getString(R.string.cancel),
                { openGpsEnableSetting() },
                { finish() }
            )
        }
    }

    fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isFirstLocationRequest=false
            val lm: LocationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            var network_enabled = false

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: java.lang.Exception) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: java.lang.Exception) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user
                enableLoc()
            } else {
                fetchLoc()
            }
        } else  {

            if(!isFirstLocationRequest) {
                showSettingsDialog(
                    getString(R.string.location_permission_request),
                    getString(R.string.open),
                    getString(R.string.cancel),
                    { openAppSettings() },
                    { finish() }
                )
            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                    isFirstLocationRequest = false
                }, 3000)

                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS_LOC,
                    SoniApp.REQUEST_CODE_PERMISSIONS
                )
            }



        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(!isFirstLocationRequest){
            getLocation()
        }
    }


    fun showSettingsDialog(
        title: String,
        yes: String,
        no: String,
        yesClick: () -> Unit,
        noClick: () -> Unit
    ) {
        if (dialog != null || isFirstLocationRequest ) {
            return
        }
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog!!.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog!!.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._195sdp).toInt()

        dialog!!.window?.setLayout(width, height)

        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_title)
        val tvMessage = dialog!!.findViewById<TextView>(R.id.tv_message)
        val txtyes = dialog!!.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog!!.findViewById<TextView>(R.id.tv_no)
        val viewSpacer = dialog!!.findViewById<View>(R.id.viewSpacer)

        tvMessage.text = getString(R.string.location_message)
        tvTitle.text = title
        txtyes.text = yes
        txtno.text = no
        txtno.visibility = View.GONE
        viewSpacer.visibility = View.GONE
        txtyes.setOnClickListener {
            dialog!!.dismiss()
            yesClick()
            dialog = null
        }
        txtno.setOnClickListener {
            dialog!!.dismiss()
            noClick()
            dialog = null
        }
        dialog!!.setCanceledOnTouchOutside(true);
        dialog!!.show()
        dialog!!.setCancelable(false)
    }

    fun showYesNOsDialog(
        title: String,
        message: String,
        yes: String,
        no: String,
        yesClick: () -> Unit,
        noClick: () -> Unit
    ) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._175sdp).toInt()

        dialog.window?.setLayout(width, height)

        val tvTitle = dialog.findViewById<TextView>(R.id.tv_title)
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message)
        val txtyes = dialog.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog.findViewById<TextView>(R.id.tv_no)
        tvMessage.text = message
        tvTitle.text = title
        txtyes.text = yes
        txtno.text = no

        txtyes.setOnClickListener {
            dialog.dismiss()
            yesClick()
        }
        txtno.setOnClickListener {
            dialog.dismiss()
            noClick()
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION) {
            when (resultCode) {
                RESULT_OK -> {
                    fetchLoc()
                }

                RESULT_CANCELED -> {
                    Log.e("GPS", "User denied to access location")
//                    showToast(getString(R.string.location_off))
//                    openGpsEnableSetting()
                    showSettingsDialog(
                        getString(R.string.location_permission_request),
                        getString(R.string.open),
                        getString(R.string.cancel),
                        { openGpsEnableSetting(); },
                        { finish() })

                }
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            getLocation()
        }
        if (requestCode == SoniApp.REQUEST_CODE_PERMISSIONS){
            if(!SoniApp.allPermissionsGranted(this)){
                showSettingsDialog(
                    getString(R.string.gallery_and_camera_permissions_required),
                    getString(R.string.open),
                    getString(R.string.cancel),
                    { openAppSettings(); },
                    { finish() })
            }
        }
    }

    private fun fetchLoc() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        locationCalllistener?.let { it(location) }
                        SoniApp.location = location;
                    } else {
                        fusedLocationClient?.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            null
                        )?.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                SoniApp.location = location
                                locationCalllistener?.let { it(location) }
                            }
                        }

                    }
                }
        }

    }

    private fun openGpsEnableSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_ENABLE_GPS)
    }

    fun openAppSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            packageName, null
        )
        intent.data = uri
        startActivityForResult(intent, REQUEST_ENABLE_GPS)
    }

    suspend fun compressImage(sourceFile: File): File {
        val dataDir =
            applicationContext.getExternalFilesDir(null)?.absolutePath // context being the Activity pointer
        val imgFolder = File(dataDir, "imgFolder")
        imgFolder.mkdirs()
        var compressedFile = File(imgFolder, sourceFile.path.split("/").last())
        val compressedImageFile = Compressor.compress(this, sourceFile) {
            default()
            quality(80)
            destination(compressedFile)
            Dispatchers.Main
        }
        try {
            // delete the original file
            sourceFile.delete()
        } catch (e: Exception) {
            Log.e("tag", e.message!!)
        }
        return compressedImageFile
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
    override fun onResume() {
        super.onResume()
//        getLocation();
    }

}