package com.soni.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.soni.Preference.*
import com.soni.SoniApp
import com.soni.R
import com.soni.SoniApp.Companion.changeAppLanguage
import com.soni.databinding.ActivityLanguageSelectBinding
import com.soni.services.web.models.OtpResponseModel
import com.soni.services.web.models.BaseModel
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LanguageSelectActivity : BaseActivity() {
    lateinit var binding : ActivityLanguageSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityLanguageSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun  init(){
        ivBack=findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }

        binding.txtSubmit.setOnClickListener {
            binding.txtSubmit.visibility = View.GONE
            binding.txtSendOtpShimmer.visibility = View.VISIBLE
            storeLanguageSet(true)
            SoniApp.changeAppLanguage(this@LanguageSelectActivity, getCurrentLanguageID())
            val intent =
                Intent(this@LanguageSelectActivity, SplashScreen::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finishAffinity()
        }
        storeCurrentLanguageID("2")
        binding.llEnglish.setOnClickListener {
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            storeCurrentLanguageID("1")

        }

        binding.llSecondLang.setOnClickListener {
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            storeCurrentLanguageID("2")
        }

    }





}