package com.soni.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.soni.Preference.StoreUserData
import com.soni.Preference.storeLoginToken
import com.soni.Preference.storeUserID
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.*
import com.soni.databinding.FragmentProfileBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.UserModel
import com.soni.utils.Const.API.BaseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : BaseFragment() {

lateinit var  binding: FragmentProfileBinding
    var RC_GALLERY_PERM = 100
    var imagePath=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    fun init(){


        onClick()

    }

    override fun onResume() {
        super.onResume()
        if (SoniApp.user!=null) {
            if (SoniApp.user!!.email == null) {
                binding.tvUserEmail.text = ""

            } else {
                binding.tvUserEmail.text = "${SoniApp.user!!.email!!}"
            }
            binding.tvUserName.text =
                if (SoniApp.user!!.firstName != null) SoniApp.user!!.firstName!! else "" + " " + if (SoniApp.user!!.lastName != null) SoniApp.user!!.lastName!! else ""

            if (!SoniApp.user!!.profileUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(BaseUrl+SoniApp.user!!.profileUrl!!)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(binding.ivUserImage)
            }
            var userName = if (SoniApp.user!!.firstName != null) SoniApp.user!!.firstName!! else ""
            binding.tvHello.text = "${getString(R.string.hello_user)} ${if(SoniApp.user!!.firstName!=null)" "+SoniApp.user!!.firstName else " "}!"
        }
    }

    private fun onClick() {

        binding.ivEdit.setOnClickListener {
            val intent = Intent(requireContext(),ViewProfileActivity::class.java)
            startActivity(intent)
        }


        binding.llChangeLang.setOnClickListener {
            val intent = Intent(requireContext(),ChangeLanguageActivity::class.java)
            startActivity(intent)
        }

        binding.llChangePass.setOnClickListener {
            val intent = Intent(requireContext(),ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.llTremsCondition.setOnClickListener {

            goToWebviewActivity(getString(R.string.terms_conditions),SoniApp.urls!!.terms_conditions_url!!)
        }

        binding.llPrivacyPolicy.setOnClickListener {

            goToWebviewActivity(getString(R.string.privacy_policy),SoniApp.urls!!.privacy_policy_url!!)
        }

        binding.llLogout.setOnClickListener {
            showLogOutDialog()

        }


        binding.tvDeleteUser.setOnClickListener {
         showLanguageDialog()
        }
    }

    private fun goToWebviewActivity(name:String,url:String) {
        val intent = Intent(requireContext(),WebViewActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("url",url)
        startActivity(intent)
    }
//    postDeleteUser
    private fun callDeleteApi() {

    if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {


        SoniApp.mApiCall?.postDeleteUser()
            ?.enqueue(object : Callback<BaseModel> {
                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))

                }

                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>?
                ) {
                    if (response != null) {
                        val responseLogin = response.body()
                        if (responseLogin != null) {
                            if (responseLogin.status!!) {

                                    var user= UserModel()
                                    storeUserID("")
                                    storeLoginToken("")
                                    StoreUserData(user)
                                    var mIntent=Intent(requireActivity()as HomeActivity,AuthenticationActivity::class.java)
                                    startActivity(mIntent)
                                    requireActivity().finishAffinity()
                                    (requireActivity() as HomeActivity).showToast(responseLogin.message!!)

                            } else {
                                var user= UserModel()
                                storeUserID("")
                                storeLoginToken("")
                                StoreUserData(user)
                                var mIntent=Intent(requireActivity()as HomeActivity,AuthenticationActivity::class.java)
                                startActivity(mIntent)
                                requireActivity().finishAffinity()
                                (requireActivity() as HomeActivity).showToast(responseLogin.message!!)

                            }
                            (requireActivity() as HomeActivity).showToast(responseLogin.message!!)

                        }
                    }
                }


            })

    } else {
        (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
      }
}

    private fun callLogOutApi() {

    if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {


        SoniApp.mApiCall?.postLogoutUser()
            ?.enqueue(object : Callback<BaseModel> {
                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))

                }

                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>?
                ) {
                    if (response != null) {
                        val responseLogin = response.body()
                        if (responseLogin != null) {
                            if (responseLogin.status!!) {
                                if (responseLogin.status!!) {
                                    var user= UserModel()
                                    storeUserID("")
                                    storeLoginToken("")
                                    StoreUserData(user)
                                    var mIntent=Intent(requireActivity()as HomeActivity,AuthenticationActivity::class.java)
                                    startActivity(mIntent)
                                    requireActivity().finishAffinity()
                                }
                            } else {
                                (requireActivity() as HomeActivity).showToast(responseLogin.message!!)
                            }


                        }
                    }
                }


            })

    } else {
        (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
      }
}
    private fun showLanguageDialog(){

        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._150sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtyes = dialog.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_title = dialog.findViewById<TextView>(R.id.tv_title)
        val tv_message = dialog.findViewById<TextView>(R.id.tv_message)

        tv_title.text=getString(R.string.delete_accoun)
        tv_message.text=getString(R.string.delete_account_msg)

        txtyes.setOnClickListener {
            dialog.dismiss()
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            callDeleteApi()
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }
    private fun showLogOutDialog(){

        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._150sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtyes = dialog.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_title = dialog.findViewById<TextView>(R.id.tv_title)
        val tv_message = dialog.findViewById<TextView>(R.id.tv_message)

        tv_title.text=getString(R.string.logout_title)
        tv_message.text=getString(R.string.log_out_message)
        txtyes.text=getString(R.string.logout_title)
        txtyes.setOnClickListener {
            dialog.dismiss()
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            callLogOutApi()
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }
}