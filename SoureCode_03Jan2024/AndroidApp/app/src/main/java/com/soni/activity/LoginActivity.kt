package com.soni.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.soni.Preference.*
import com.soni.R
import com.soni.SoniApp
import com.soni.SoniApp.Companion.changeAppLanguage
import com.soni.databinding.ActivityLoginBinding
import com.soni.services.web.models.OtpResponseModel
import com.soni.services.web.models.UserLogin
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        changeAppLanguage(this, getCurrentLanguageID())
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init(){
       ivBack=findViewById(R.id.iv_back)
       ivBack.setOnClickListener {
           finish()
       }
        binding.txtSignin.setOnClickListener {
            if (validateInput()) {
                binding.txtSignin.visibility = View.GONE
                binding.txtSendOtpShimmer.visibility = View.VISIBLE
                callApi()

            } else {
                showToast(error)
            }

        }

        binding.txtForgetPassword.setOnClickListener {
            val intent = Intent(this,ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.ivShowPassword.setOnClickListener {
            togglePassVisability(binding.etPassword.text.toString(),binding.etPassword,binding.ivShowPassword)
        }
        binding.etEmail.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llEmail,b)
        }
        binding.etPassword.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llPassword,b)
        }
    }

    fun validateInput(): Boolean {
        val MobilePattern = "[0-9]{10}"
//        if (binding.etEmail.text.toString().trim().isEmpty()) {
//            error = getString(R.string.add_email_or_number)
//            return false
//        }else  if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
//                binding.etEmail.text.toString().trim()
//            ).matches() && ! binding.etEmail.text.toString().trim().matches(Regex(MobilePattern))) {
//            error = getString(R.string.invalid_email_or_number)
//            return false
//        }
//        else if (binding.etPassword.text.toString().trim().isEmpty()) {
//            error = getString(R.string.invalid_login_password)
//            return false
//        } else if (binding.etPassword.text.toString().trim().length < 8) {
//            error = getString(R.string.invalid_password)
//            return false
//        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString().trim()
            ).matches() && ! binding.etEmail.text.toString().trim().matches(Regex(MobilePattern))) {
            error = getString(R.string.invalid_phone_number)
            return false
        }
        return true

    }

    private fun callApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            param["phone_number"] = binding.etEmail.text.toString().trim()
//            param["password"] = binding.etPassword.text.toString().trim()

            SoniApp.mApiCall?.postSigIn(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSignin.visibility = View.VISIBLE
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
//                                    showToast(responseLogin.otp!!.toString())
                                    val intent =
                                        Intent(this@LoginActivity, OtpActivity::class.java)
                                    intent.putExtra("ScreenId",2)
                                    intent.putExtra(Const.IntentKey.PhoneNumber,binding.etEmail.text.toString().trim())
                                    startActivity(intent)

                                }else{
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.txtSignin.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtSignin.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE

        }
    }

}