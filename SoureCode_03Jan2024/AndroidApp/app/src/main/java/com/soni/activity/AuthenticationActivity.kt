package com.soni.activity

import android.content.Intent
import android.os.Bundle
import com.soni.Preference.StoreUserData
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.storeLoginToken
import com.soni.Preference.storeUserID
import com.soni.SoniApp
import com.soni.databinding.ActivityAuthenticationBinding
import com.soni.services.web.models.UserModel
import com.soni.utils.Const

class AuthenticationActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding=ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.extras!=null){
            if(intent.getBooleanExtra(Const.IntentKey.Unauthorized,false)){
                var user= UserModel()
                storeUserID("")
                storeLoginToken("")
                StoreUserData(user)
                showToast("Session Expired")
            }
        }
        init()
    }

    private fun init(){

        binding.tvSignIn.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)

        }
        binding.llSignUp.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}