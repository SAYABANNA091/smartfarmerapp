package com.soni

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDexApplication
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.soni.Preference.getURLS
import com.soni.Preference.initPreferences
import com.soni.Preference.storeCurrentLanguageID
import com.soni.activity.ViewProfileActivity
import com.soni.adapter.MessingItemListAdapter
import com.soni.services.web.ApiCall
import com.soni.services.web.RetrofitAPI
import com.soni.services.web.models.*
import com.soni.utils.Const
import com.soni.utils.MapCallBack
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class SoniApp : MultiDexApplication() {

    companion object{
        var mRetrofitAPI: Retrofit? = null
        var mApiCall: ApiCall? = null
        var user: UserModel? = null
        var propertyType    : ArrayList<PropertyType> = arrayListOf()
        var productType    : ArrayList<ProductType> = arrayListOf()
        var stateList:ArrayList<State> = arrayListOf()
        var urls    : StaticUrls?=null
        var fusedLocationClient: FusedLocationProviderClient?=null
        var isFirstLocationRequest:Boolean =true
        var productData: ProductData? = null
        var missingPopup:Dialog?=null
        var detailsList: ArrayList<String> = arrayListOf()
        var location : Location?=null;
        var mapCallbacks:ArrayList<MapCallBack> = arrayListOf()


        private fun setApplicationLanguage(context: Context, newLanguage: String){
            val activityRes = context.resources
            val activityConf: Configuration = activityRes.configuration
            val newLocale = Locale(newLanguage)
            Locale.setDefault(newLocale)
            activityConf.setLocale(newLocale)

            activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
            val applicationRes = context.resources
            val applicationConf: Configuration = applicationRes.configuration
            applicationConf.setLocale(newLocale)

            applicationRes.updateConfiguration(
                applicationConf,
                applicationRes.displayMetrics
            )

        }
        const val REQUEST_CODE_PERMISSIONS = 10
        var REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,

            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }.toTypedArray()

        fun allPermissionsGranted(baseContext: Activity) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
        fun allPermissionsDenied(baseContext: Activity): Boolean {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                } else {
                    return false
                }
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                } else {
                    return false
                }
            }
            else {
                return false
            }
        }


        fun checkNetworkPermission(baseContext: Activity) = ContextCompat.checkSelfPermission(
            baseContext, Manifest.permission.ACCESS_NETWORK_STATE ) == PackageManager.PERMISSION_GRANTED

        fun changeAppLanguage(context: Context, languageType: String) {
            val languageCode: String = when (languageType) {
                "2" -> Const.Language.KANNADA
                "1" -> Const.Language.ENGLISH
                else -> Const.Language.ENGLISH
            }
            when(languageType){
                "2" -> storeCurrentLanguageID("2")
                "1" -> storeCurrentLanguageID("1")
                else -> storeCurrentLanguageID("1")

            }
            setApplicationLanguage(
                context,
                languageCode
            )
        }
        fun Context.getDeviceId(): String {
            return Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        fun getAndroidVersion(): String? {
            val release: String = Build.VERSION.RELEASE
            return release
        }

        fun getDeviceName(): String? {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model
            } else manufacturer + " " + model
        }

        fun onCreate(activity: Activity) {

            mRetrofitAPI = RetrofitAPI.getRetrofit(activity)
            mApiCall = mRetrofitAPI?.create(ApiCall::class.java)
            activity.initPreferences()
            checkNetworkPermission(activity)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            urls=getURLS()


        }

        fun copyFile(inputPath: String?, outputPath: String?) {
            var out: OutputStream? = null
            try {
                val myBitmap = BitmapFactory.decodeFile(inputPath)

                out = FileOutputStream(outputPath)

                val byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.JPEG,25,out)


                out.flush()
                out.close()
                out = null
            } catch (fnfe1: FileNotFoundException) {
                Log.e("tag", fnfe1.message!!)
            } catch (e: Exception) {
                Log.e("tag", e.message!!)
            }
        }

        fun getWeatherIcon(pos:Int): Int {
            var iconId=     when (pos) {
                1->R.drawable.weather_icon_01
                2->R.drawable.weather_icon_01
                3->R.drawable.weather_icon_01
                4->R.drawable.weather_icon_03
                5->R.drawable.weather_icon_03
                6->R.drawable.weather_icon_03
                7->R.drawable.weather_icon_02
                8->R.drawable.weather_icon_02
                9->R.drawable.weather_icon_02
                10->R.drawable.weather_icon_02
                11->R.drawable.weather_icon_02
                12->R.drawable.weather_icon_04
                13->R.drawable.weather_icon_04
                14->R.drawable.weather_icon_04
                15->R.drawable.weather_icon_06
                16->R.drawable.weather_icon_16
                17->R.drawable.weather_icon_16
                18->R.drawable.weather_icon_18
                19->R.drawable.weather_icon_07
                20->R.drawable.weather_icon_07
                21->R.drawable.weather_icon_07
                22->R.drawable.weather_icon_10
                23->R.drawable.weather_icon_10
                24->R.drawable.weather_icon_20
                25->R.drawable.weather_icon_10
                26->R.drawable.weather_icon_10
                27->R.drawable.weather_icon_10
                28->R.drawable.weather_icon_10
                29->R.drawable.weather_icon_10
                30->R.drawable.weather_icon_21
                31->R.drawable.weather_icon_22
                32->R.drawable.weather_icon_12
                33->R.drawable.weather_icon_08
                34->R.drawable.weather_icon_09
                35->R.drawable.weather_icon_09
                36->R.drawable.weather_icon_09
                37->R.drawable.weather_icon_09
                38->R.drawable.weather_icon_09
                39->R.drawable.weather_icon_04
                40->R.drawable.weather_icon_04
                41->R.drawable.weather_icon_04
                42->R.drawable.weather_icon_06
                43->R.drawable.weather_icon_06
                44->R.drawable.weather_icon_06
                else -> R.drawable.weather_icon_01
            }
            return iconId
        }


        fun showInquiryPopup( context: Context,details:ArrayList<String> ) {
            val dialog = Dialog(context)
            missingPopup=dialog
            detailsList=details
            dialog.setContentView(R.layout.layout_missing_details_pop);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.window?.setBackgroundDrawable(context.getDrawable(R.drawable.dialog_back))
            dialog.setCancelable(false);


            val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
            val height = context.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._345sdp).toInt()

            dialog.window?.setLayout(width, height)

            val txtOk = dialog.findViewById<TextView>(R.id.txt_ok)
            txtOk.setText(R.string.add)
            var detailsAdapter= MessingItemListAdapter(context,details)
            val rvMissingDetails = dialog.findViewById<RecyclerView>(R.id.rv_missing_details)
            rvMissingDetails.layoutManager  = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvMissingDetails.adapter=detailsAdapter

            txtOk.setOnClickListener {
                var mIntent= Intent(context, ViewProfileActivity::class.java)
                mIntent.putExtra("details",details)
                context.startActivity(mIntent)
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(true);
            dialog.show()

        }
    }


}