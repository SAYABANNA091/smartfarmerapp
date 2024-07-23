package com.soni.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.soni.Preference.*
import com.soni.R
import com.soni.SoniApp
import com.soni.SoniApp.Companion.productType
import com.soni.databinding.ActivitySplashScreenBinding
import com.soni.services.web.models.*
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreen : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    var isLogedIn=false
    var propertyType : ArrayList<PropertyType> = arrayListOf()
    var hasLanguageSet=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val notificationId=intent.getIntExtra(Const.IntentKey.notificationId,-1)
        if(notificationId!=-1) {
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.cancel(notificationId)
        }
        SoniApp.onCreate(this)

        propertyType.add(PropertyType(null,"Tractor",null,null,false))
        propertyType.add(PropertyType(null,"JCB",null,null,false))
        propertyType.add(PropertyType(null,"Farms",null,null,false))
        propertyType.add(PropertyType(null,"Nursery",null,null,false))
        propertyType.add(PropertyType(null,"Vermicompost",null,null,false))

        productType.add(ProductType(null,"Animal",null,null,false))
        productType.add(ProductType(null,"Agri Tools",null,null,false))
        productType.add(ProductType(null,"Agri Products",null,null,false))

        init()
    }
    private fun init(){
        var user=getUserData()
        callGetPropertyTypes()
        callGetProductsTypes()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            storeFCMToken(token)
            Firebase.messaging.isAutoInitEnabled = true
            Log.d("FCM", token)
        })
       if(user != null) {
            if (user?.id != null) {
                isLogedIn = true
            }

           callStaticUlsApi()

       }
        hasLanguageSet= getLanguageSet()
        Handler(Looper.getMainLooper()).postDelayed({
            if (hasLanguageSet) {
                if (isLogedIn) {
                    callApi()

                } else {
                    val intent = Intent(this, AuthenticationActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }else{
                val intent = Intent(this, LanguageSelectActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)


    }


    private fun callApi() {
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.postGetUser()
                ?.enqueue(object : Callback<GetUser> {
                    override fun onFailure(call: Call<GetUser>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                        SoniApp.user=getUserData()
                        val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onResponse(
                        call: Call<GetUser>,
                        response: Response<GetUser>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
                                    if (responseLogin.status!!){
                                        StoreUserData(responseLogin!!.data!!)
                                        SoniApp.user=responseLogin!!.data!!
                                        val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }else{
//                                    showToast(responseLogin.message!!)
                                    if(responseLogin.message=="Invalid Header"){

                                    }else{
                                        SoniApp.user=getUserData()
                                        val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    }

                                    Log.d("SPLASH ERROR","DATA Not Found")

                                }
                            }else{
                                SoniApp.user=getUserData()
                                val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }else{
                            SoniApp.user=getUserData()
                            val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                       }


                })

        } else {
            showToast(getString(R.string.internet_error))
            SoniApp.user=getUserData()
            val intent = Intent(this@SplashScreen, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
    private fun callStaticUlsApi() {
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.getStaticUls()
                ?.enqueue(object : Callback<StaticUrlsResponse> {
                    override fun onFailure(call: Call<StaticUrlsResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<StaticUrlsResponse>,
                        response: Response<StaticUrlsResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
                                    if (responseLogin.status!!){
                                        StoreURLS(responseLogin!!.data!!)
                                        SoniApp.urls=responseLogin!!.data!!
                                    }
                                }else{
//                                    showToast(responseLogin.message!!)
                                    Log.d("SPLASH ERROR","DATA Not Found")

                                }
                            }
                        }
                       }


                })

        } else {
            showToast(getString(R.string.internet_error))

        }
    }
    private fun callGetPropertyTypes() {
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.postGetPropertyTypes()
                ?.enqueue(object : Callback<PropertyTypeResponse> {
                    override fun onFailure(call: Call<PropertyTypeResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                        SoniApp.propertyType.clear()
                        SoniApp.propertyType.addAll(propertyType)
                    }

                    override fun onResponse(
                        call: Call<PropertyTypeResponse>,
                        response: Response<PropertyTypeResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
                                    SoniApp.propertyType.clear()

                                      SoniApp.propertyType.addAll(responseLogin.propertyType)
                                }else{
//                                    showToast(responseLogin.message!!)
                                    Log.d("SPLASH ERROR","DATA Not Found")
                                    SoniApp.propertyType.clear()

                                    SoniApp.propertyType.addAll(propertyType)
                                }
                            }else{
                                SoniApp.propertyType.clear()

                                SoniApp.propertyType.addAll(propertyType)
                            }
                        }else{
                            SoniApp.propertyType.clear()

                            SoniApp.propertyType.addAll(propertyType)
                        }
                       }


                })

        } else {
            showToast(getString(R.string.internet_error))
            SoniApp.propertyType.clear()

            SoniApp.propertyType.addAll(propertyType)

        }
    }
    private fun callGetProductsTypes() {
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.postGetProductsTypes()
                ?.enqueue(object : Callback<ProductCategoryResponse> {
                    override fun onFailure(call: Call<ProductCategoryResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<ProductCategoryResponse>,
                        response: Response<ProductCategoryResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
                                      productType.clear()
                                      productType.addAll(responseLogin.data)
                                }else{
//                                    showToast(responseLogin.message!!)
                                    Log.d("SPLASH ERROR","DATA Not Found")
                                }
                            }else{
                               }
                        }else{
                        }
                       }


                })

        } else {
            showToast(getString(R.string.internet_error))
            SoniApp.propertyType.clear()

            SoniApp.propertyType.addAll(propertyType)

        }
    }


}