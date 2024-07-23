package com.soni.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.storeLoginToken
import com.soni.Preference.storeUserID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityOtpBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.OtpResponseModel
import com.soni.services.web.models.UserLogin
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects


class OtpActivity : BaseActivity() {
    lateinit var binding: ActivityOtpBinding
    var email: String = ""
    var phone: String = ""
    var time = SystemClock.elapsedRealtime()
    var isOtpExpired = false
    var isForgot = false

    //SignUp Items
    var first_name = ""
    var last_name = ""
    var password = ""
    var confirm_password = ""
    var state = ""
    var district = ""
    var taluka = ""
    var village = ""
    var pincode = ""

    lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        email = intent.extras!!.getString(Const.IntentKey.Email, "")
        phone = intent.extras!!.getString(Const.IntentKey.PhoneNumber, "")
        init()
    }

    private fun init() {
        ivBack = findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }

        binding.tvResendOtp.visibility=View.INVISIBLE
        if (intent.getIntExtra("ScreenId", 1) == 1) {
            isForgot = true
            binding.txtSignUp.text = getString(R.string.confim)

        } else {
            binding.txtSignUp.text = getString(R.string.confim)

        }
        binding.txtSignUp.setOnClickListener {

            if (binding.firstPinView.text.toString().length < 4) {
                if (intent.getIntExtra("ScreenId", 0) != 1){
                    showToast(getString(R.string.add_otp_mobile))
                }
               else if(email.isEmpty() ){
                    showToast(getString(R.string.add_otp_mobile))
                }else {
                    showToast(getString(R.string.add_otp_email))
                }
            } else if (isOtpExpired) {
                showToast(getString(R.string.otp_expired))
            } else {
                if (intent.getIntExtra("ScreenId", 0) == 1)
                {
                    callApi()
                }else{
                    signupCall()
                }
                binding.txtSignUp.visibility = View.GONE
                binding.txtSendOtpShimmer.visibility = View.VISIBLE


            }
        }

        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.length == 4) {
                    hideKeyboard(binding.firstPinView)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.tvTimer.isCountDown = true
        binding.tvTimer.base = time + 120000
        binding.tvTimer.start()
        timer= object : CountDownTimer(120000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                //add your code here
                isOtpExpired = true
                binding.tvTimer.stop()
                binding.tvResendOtp.visibility=View.VISIBLE
            }
        }.start()

       binding.tvResendOtp.setOnClickListener {
           binding.tvResendOtp.visibility=View.INVISIBLE
           if (isOtpExpired) {
               if (intent.getIntExtra("ScreenId", 0) == 1) {
                   callForgotPasswordApi()
               } else {
                   callSignUpApi()
               }
           }
       }
    }

    private fun signupCall(){
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            param["otp"] = binding.firstPinView.text.toString().trim()
            param["phone_number"] = phone

            SoniApp.mApiCall?.postVerifyOtpSignUP(param)
                ?.enqueue(object : Callback<UserLogin> {
                    override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<UserLogin>,
                        response: Response<UserLogin>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!=null) {
                                    if (responseLogin.status!!) {
//                                    showToast(responseLogin!!.message!!)
                                        storeLoginToken(responseLogin!!.data!!.token!!)
                                        StoreUserData(responseLogin!!.data!!.user!!)
                                        storeUserID(responseLogin!!.data!!.user!!.id!!.toString())
                                        SoniApp.user = responseLogin!!.data!!.user!!
                                        callLanguageApi()

                                        binding.txtSignUp.visibility = View.VISIBLE
                                        binding.txtSendOtpShimmer.visibility = View.GONE
                                    } else {
                                        showToast(responseLogin.message!!)
                                        binding.txtSignUp.visibility = View.VISIBLE
                                        binding.txtSendOtpShimmer.visibility = View.GONE
                                    }
                                }
                            }
                        }

                    }


                })

        } else {
            binding.txtSignUp.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE
            showToast(getString(R.string.internet_error))

        }




    }


    private fun callApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            param["otp"] = binding.firstPinView.text.toString().trim()
            param["phone_number"] = phone

            SoniApp.mApiCall?.postVerifyOtp(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<OtpResponseModel>,
                        response: Response<OtpResponseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    showToast(responseLogin!!.message!!)
                                    val intent = Intent(
                                        this@OtpActivity,
                                        ResetPasswordActivity::class.java
                                    )
                                    intent.putExtra(Const.IntentKey.Email, email)
                                    intent.putExtra(Const.IntentKey.PhoneNumber, phone)
                                    intent.putExtra(
                                        Const.IntentKey.OTP,
                                        binding.firstPinView.text.toString().trim()
                                    )
                                    startActivity(intent)

                                    binding.txtSignUp.visibility = View.VISIBLE
                                    binding.txtSendOtpShimmer.visibility = View.GONE
                                } else {
                                    showToast(responseLogin.message!!)
                                    binding.txtSignUp.visibility = View.VISIBLE
                                    binding.txtSendOtpShimmer.visibility = View.GONE
                                }
                            }
                        }

                    }


                })

        } else {
            binding.txtSignUp.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE
            showToast(getString(R.string.internet_error))

        }
    }


    private fun callSignUpApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            param["phone_number"] = phone

            SoniApp.mApiCall?.postSignUp(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<OtpResponseModel>,
                        response: Response<OtpResponseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
//                                    showToast(responseLogin.otp!!.toString())
                                    binding.tvResendOtp.visibility=View.INVISIBLE
                                    binding.tvTimer.isCountDown = true
                                    binding.tvTimer.base = SystemClock.elapsedRealtime() + 120000
                                    binding.tvTimer.start()
                                    timer= object : CountDownTimer(120000, 1000) {
                                        override fun onTick(p0: Long) {}
                                        override fun onFinish() {
                                            //add your code here
                                            isOtpExpired = true
                                            binding.tvTimer.stop()
                                            binding.tvResendOtp.visibility=View.VISIBLE
                                        }
                                    }.start()
                                    isOtpExpired = false
                                } else {
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtSignUp.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE
        }
    }
    private fun callForgotPasswordApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            if (email.isNotEmpty()) {
                param["email"] =email
            }else{
                param["phone_number"] =phone
            }

            SoniApp.mApiCall?.postForgotPassword(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<OtpResponseModel>,
                        response: Response<OtpResponseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
//                                  showToast(responseLogin.otp!!.toString())
                                    binding.tvResendOtp.visibility=View.INVISIBLE
                                    binding.tvTimer.isCountDown = true
                                    binding.tvTimer.base = SystemClock.elapsedRealtime() + 120000
                                    binding.tvTimer.start()
                                    timer= object : CountDownTimer(120000, 1000) {
                                        override fun onTick(p0: Long) {}
                                        override fun onFinish() {
                                            //add your code here
                                            isOtpExpired = true
                                            binding.tvTimer.stop()
                                            binding.tvResendOtp.visibility=View.VISIBLE
                                        }
                                    }.start()
                                    isOtpExpired = false
                                }else{
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.txtSignUp.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtSignUp.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE
        }
    }

    private fun callLanguageApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()

            param["language_id"] = getCurrentLanguageID()
            param["phone_number"] = phone

            SoniApp.mApiCall?.postSetLanguage (param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))

                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!){
                                    showLanguageDialog()
                                }

                            }
                        }

                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
           }
    }

    private fun showLanguageDialog(){

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_language_selected_cong);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._300sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtOk = dialog.findViewById<TextView>(R.id.txt_ok)
        txtOk.setOnClickListener {
//            com.soni.SoniApp.changeAppLanguage(this,)
            dialog.dismiss()
            SoniApp.changeAppLanguage(this@OtpActivity, getCurrentLanguageID())
            val intent =
                Intent(this@OtpActivity, SplashScreen::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finishAffinity()

        }
        dialog.show()
    }
    override fun onResume() {
        Log.d("Splash","OnResume")
        super.onResume()

    }
}