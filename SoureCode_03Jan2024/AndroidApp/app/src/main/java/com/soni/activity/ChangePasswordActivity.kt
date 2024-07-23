package com.soni.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityChangePasswordBinding
import com.soni.services.web.models.BaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordActivity : BaseActivity() {

    lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        //setContentView(R.layout.activity_change_password)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }


    fun init() {
        binding.ivCurrentPass.setOnClickListener {
            togglePassVisability(binding.edtCurrentPass.text.toString(),binding.edtCurrentPass,binding.ivCurrentPass)
        }

        binding.ivNewPass.setOnClickListener {
            togglePassVisability(binding.edtNewPass.text.toString(),binding.edtNewPass,binding.ivNewPass)
        }

        binding.ivConfNewPass.setOnClickListener {
            togglePassVisability(binding.ConfirmNewPassword.text.toString(),binding.ConfirmNewPassword,binding.ivConfNewPass)
        }

        ivBack = findViewById(R.id.iv_back)
        ivRight = findViewById(R.id.iv_right)
        title = findViewById(R.id.tv_title)
        title.setText(getString(R.string.change_password))
        ivRight.visibility = View.INVISIBLE

        ivBack.setOnClickListener { finish() }
        binding.tvConfirmPassword .setOnClickListener {
            if (validateInput()) {
            binding.tvConfirmPassword.visibility = View.GONE
            binding.txtSendOtpShimmer.visibility = View.VISIBLE
            callApi()

        } else {
            showToast(error)
        } }

        binding.edtCurrentPass.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llCurrentPassword,b)
        }
        binding.edtNewPass.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llNewPass,b)
        }
        binding.ConfirmNewPassword.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llConfirmNewPassword,b)
        }



    }

    fun validateInput(): Boolean {
        if (binding.edtCurrentPass.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_current_password)
            return false
        } else if (binding.edtCurrentPass.text.toString().length<8) {
            error = getString(R.string.invalid_current_password)
            return false
        } else if (binding.edtNewPass.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_new_password)
            return false
        } else if (binding.edtNewPass.text.toString().length<8) {
            error = getString(R.string.invalid_new_password_length)
            return false
        } else if (binding.ConfirmNewPassword.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_confirm_password)
            return false
        }  else if (binding.edtCurrentPass.text.toString()==binding.edtNewPass.text.toString()) {
            error = getString(R.string.new_old_password_match)
            return false
        }else if (binding.edtNewPass.text.toString()!=binding.ConfirmNewPassword.text.toString()) {
            error = getString(R.string.new_and_confirm_not_match)
            return false
        }
        return true
    }

    private fun callApi() {
        if (isInternetAvailable(this)) {
            val param = HashMap<String, String>()
            param["old_password"] = binding.edtCurrentPass.text.toString()
            param["new_password"] = binding.edtNewPass.text.toString()
            param["confirm_password"] = binding.ConfirmNewPassword.text.toString()
        
            SoniApp.mApiCall?.postChangePassword(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.tvConfirmPassword.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!)  {
                                    if (responseLogin.status!!){
                                        showToast(responseLogin!!.message!!)
                                       finish()
                                    }

                                }else{
                                    showToast(responseLogin.message!!)

                                }
                            }
                        }
                        binding.tvConfirmPassword.visibility = View.VISIBLE
                        binding.txtSendOtpShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.tvConfirmPassword.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE

        }
    }
//    change-password
}