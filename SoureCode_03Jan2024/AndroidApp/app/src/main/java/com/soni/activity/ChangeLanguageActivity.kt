package com.soni.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide.init
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.storeCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityChangeLanguageBinding
import com.soni.services.web.models.BaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeLanguageActivity : BaseActivity() {
    lateinit var binding: ActivityChangeLanguageBinding
    var language:String= getCurrentLanguageID()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        //  setContentView(R.layout.activity_change_language)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        ivBack = findViewById(R.id.iv_back)
        ivRight = findViewById(R.id.iv_right)
        title = findViewById(R.id.tv_title)

        title.text = getString(R.string.Change_Language)
        ivRight.visibility = View.INVISIBLE
        ivBack.setOnClickListener {


        }

        binding.llEnglish.setOnClickListener {
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            language="1"
            storeCurrentLanguageID(language)
            callApi()

        }

        binding.llSecondLang.setOnClickListener {
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            language="2"
            storeCurrentLanguageID(language)
            callApi()
        }
        if (language=="1"){
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            language="1"
        }else{
            binding.ivEnglish.setImageDrawable(resources.getDrawable(R.drawable.not_selected_radio))
            binding.ivKanad.setImageDrawable(resources.getDrawable(R.drawable.selected_radio))
            language="2"
        }

    }


    private fun callApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()

            param["language_id"] = getCurrentLanguageID()
            param["phone_number"] = SoniApp.user!!.phoneNumber.toString()

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
                                    SoniApp.changeAppLanguage(this@ChangeLanguageActivity, getCurrentLanguageID())
                                    val intent =
                                        Intent(this@ChangeLanguageActivity, SplashScreen::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finishAffinity()
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
            dialog.dismiss()


        }
        dialog.show()
    }

}