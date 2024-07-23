package com.soni.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.storeLoginToken
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityForgetPasswordBinding
import com.soni.services.web.models.OtpResponseModel
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : BaseActivity() {
    lateinit var binding:ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    override fun onResume() {
        Log.d("Splash","OnResume")
        super.onResume()

    }
    private fun init(){
        ivBack=findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }
        binding.txtSendCode.setOnClickListener {
            if (validateInput()) {
                binding.txtSendCode.visibility = View.GONE
                binding.txtShimmer.visibility = View.VISIBLE
                callApi()

            } else {
                showToast(error)
            }
        }
        binding.etEmail.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llEmail,b)
        }
        binding.etPhoneNumber.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llPhone,b)
        }
    }

    fun validateInput(): Boolean {
        if (binding.etEmail.text.toString().trim().isNotEmpty()&&binding.etPhoneNumber.text.toString().trim().isNotEmpty()){
            error=getString(R.string.add_email_or_phone)
            return false
        }
        else if(binding.etEmail.text.toString().trim().isNotEmpty()&&binding.etPhoneNumber.text.toString().trim().isEmpty()){
            if (binding.etEmail.text.toString().trim()
                    .isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                    binding.etEmail.text.toString().trim()
                ).matches()
            ) {
                error = getString(R.string.invalid_email)
                return false
            }
        }else  if(binding.etEmail.text.toString().trim().isEmpty()&&binding.etPhoneNumber.text.toString().trim().isNotEmpty()){
            if (binding.etPhoneNumber.text.toString().trim().length < 10) {
            error = getString(R.string.invalid_phone_number)
            return false
        }
        }else if(binding.etEmail.text.toString().trim().isEmpty()&&binding.etPhoneNumber.text.toString().trim().isEmpty()){
            error=getString(R.string.add_email_or_phone)
            return false
        }

        return true
    }
    private fun callApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            if (binding.etEmail.text.toString().trim().isNotEmpty()) {
                param["email"] = binding.etEmail.text.toString().trim()
            }else{
                param["phone_number"] = binding.etPhoneNumber.text.toString().trim()
            }

            SoniApp.mApiCall?.postForgotPassword(param)
                ?.enqueue(object : Callback<OtpResponseModel> {
                    override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.txtSendCode.visibility = View.VISIBLE
                        binding.txtShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<OtpResponseModel>,
                        response: Response<OtpResponseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
//                                        showToast(responseLogin!!.otp.toString())
                                        val intent = Intent(this@ForgetPasswordActivity,OtpActivity::class.java)
                                        intent.putExtra("ScreenId",1)
                                        intent.putExtra(Const.IntentKey.Email,binding.etEmail.text.toString().trim())
                                        intent.putExtra(Const.IntentKey.PhoneNumber,binding.etPhoneNumber.text.toString().trim())

                                        startActivity(intent)


                                }else{
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.txtSendCode.visibility = View.VISIBLE
                        binding.txtShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtSendCode.visibility = View.VISIBLE
            binding.txtShimmer.visibility = View.GONE
        }
    }

}