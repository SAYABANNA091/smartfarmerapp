package com.soni.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.storeLoginToken
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityResetPasswordBinding
import com.soni.services.web.models.OtpResponseModel
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    var email:String=""
    var phone:String=""
    var otp:String=""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        email=intent.extras!!.getString(Const.IntentKey.Email,"")
        phone=intent.extras!!.getString(Const.IntentKey.PhoneNumber,"")
        otp=intent.extras!!.getString(Const.IntentKey.OTP,"")

        init()
    }
    private fun init(){
        ivBack=findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }
        binding.txtConfimPassword.setOnClickListener {
            if (validateInput()) {
                binding.txtConfimPassword.visibility = View.GONE
                binding.txtSendOtpShimmer.visibility = View.VISIBLE
                callApi()

            } else {
                showToast(error)
            }
        }

        binding.ivShowPassword.setOnClickListener {
            togglePassVisability(binding.etPassword.text.toString(),binding.etPassword,binding.ivShowPassword)
        }
        binding.ivShowCPassword.setOnClickListener {
            toggleCPassVisability(binding.etCPassword.text.toString(),binding.etCPassword,binding.ivShowCPassword)
        }

        binding.etPassword.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llPassword,b)
        }
        binding.etCPassword.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llConfirmPassword,b)
        }
    }

    fun validateInput(): Boolean {
        if (binding.etPassword.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_new_password)
            return false
        } else if (binding.etPassword.text.toString().length<8) {
            error = getString(R.string.invalid_new_password_length)
            return false
        } else if (binding.etCPassword.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_confirm_password)
            return false
        }  else if (binding.etCPassword.text.toString()!=binding.etPassword.text.toString()) {
            error = getString(R.string.new_and_confirm_not_match)
            return false
        }
        return true
    }

    private fun callApi() {
        if (isInternetAvailable(this)) {
            val param = HashMap<String, String>()
            param["email"] = email
            param["phone_number"] = phone
            param["new_password"] = binding.etPassword.text.toString().trim()
            param["otp"] = otp

            SoniApp.mApiCall?.postResetPassword(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtConfimPassword.visibility = View.VISIBLE
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
                                    if (responseLogin.status!!){
                                        var mIntent=Intent(this@ResetPasswordActivity,AuthenticationActivity::class.java)
                                        finishAffinity()
                                        startActivity(mIntent)
                                    }
                                    showToast(responseLogin!!.message!!)
                                }else{
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.txtConfimPassword.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtConfimPassword.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE

        }
    }

}