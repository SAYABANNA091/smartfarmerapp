package com.soni.activity

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.DistrictSpinnerAdapter
import com.soni.adapter.StateSpinnerAdapter
import com.soni.adapter.TalukaSpinnerAdapter
import com.soni.databinding.ActivitySignUpBinding
import com.soni.services.web.models.*
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field

class SignUpActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var stateAdapter: StateSpinnerAdapter
    lateinit var districtAdapter: DistrictSpinnerAdapter
    lateinit var talukaAdapter: TalukaSpinnerAdapter
    var selectedState = 0
    var selectedDistrict = 0
    var selectedTaluka = 0
    var districtList: ArrayList<District> = arrayListOf()
    var talukaList: ArrayList<Taluka> = arrayListOf()
    var stateList: ArrayList<State> = arrayListOf()
    val popup: Field = AppCompatSpinner::class.java.getDeclaredField("mPopup")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()

    }

    private fun init() {
        ivBack = findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }


        val wordtoSpan: Spannable =
            SpannableString(getString(R.string.by_signing_up_you_agree_to_our_term_of_use_and))

        wordtoSpan.setSpan(
            ForegroundColorSpan(this.getColor(R.color.Primary100)),
            30,
            42,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtTremsOfUseText.text = wordtoSpan

        val privacy: Spannable = SpannableString(getString(R.string.privacy_notice))

        wordtoSpan.setSpan(
            ForegroundColorSpan(this.getColor(R.color.light_blue_900)),
            5,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtPrivacy.text = privacy
        binding.txtTremsOfUseText.setOnClickListener {

            goToWebviewActivity(
                getString(R.string.terms_conditions), SoniApp.urls!!.terms_conditions_url!!
            )
        }

        binding.txtPrivacy.setOnClickListener {

            goToWebviewActivity(
                getString(R.string.privacy_policy), SoniApp.urls!!.privacy_policy_url!!
            )
        }
        binding.ivShowPassword.setOnClickListener {
            togglePassVisability(
                binding.etPassword.text.toString(), binding.etPassword, binding.ivShowPassword
            )
        }
        binding.ivShowCPassword.setOnClickListener {
            toggleCPassVisability(
                binding.etCPassword.text.toString(), binding.etCPassword, binding.ivShowCPassword
            )
        }


        binding.txtSendOtp.setOnClickListener {
            if (validateInput()) {
                binding.txtSendOtp.visibility = View.GONE
                binding.txtSendOtpShimmer.visibility = View.VISIBLE
                callApi()

            } else {
                showToast(error)
            }
        }

        binding.etFirstName.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llFirstName, hasFocus)
        }
        binding.etLastName.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llLastName, hasFocus)
        }
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llEmail, hasFocus)
        }
        binding.etPhoneNumber.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llPhone, hasFocus)
        }
        binding.etPassword.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llPassword, hasFocus)
        }
        binding.etCPassword.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llConfirmPassword, hasFocus)
        }
        binding.etState.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llState, hasFocus)
        }
        binding.etDisrict.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llDistrict, hasFocus)
        }
        binding.etTaluka.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llTaluka, hasFocus)
        }
        binding.etVillage.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llVillage, hasFocus)
        }
        binding.etPincode.setOnFocusChangeListener { view, hasFocus ->
            onFocusChange(binding.llPincode, hasFocus)
        }
        setStateSpinner()
        setDistrictSpinner()
        setTalukaSpinner()
        callStateListApi()

    }

    private fun goToWebviewActivity(name: String, url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    fun validateInput(): Boolean {

        //        else if (!binding.etFirstName.text.toString().trim().matches( Regex("^[A-Za-z]+$"))){
//            error=getString(R.string.invalid_first_name)
//            return false
//        }
//        else if (!binding.etLastName.text.toString().trim().matches( Regex("^[A-Za-z]+$"))){
//            error=getString(R.string.invalid_last_name)
//            return false
//        }

        if (binding.etFirstName.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_first_name)
            return false
        } else if (binding.etLastName.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_last_name)
            return false
        } else if (binding.etEmail.text.toString().trim()
                .isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString().trim()
            ).matches()
        ) {
            error = getString(R.string.invalid_email)
            return false
        } else if (binding.etPhoneNumber.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_mobile_number)
            return false
        } else if (binding.etPhoneNumber.text.toString().trim().length < 10) {
            error = getString(R.string.invalid_phone_number)
            return false
        } else if (binding.etPassword.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_password)
            return false
        } else if (binding.etPassword.text.toString().trim().length < 8) {
            error = getString(R.string.invalid_sign_up_password)
            return false
        } else if (binding.etCPassword.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_confirm_password)
            return false
        } else if (binding.etCPassword.text.toString().trim().length < 8) {
            error = getString(R.string.invalid_passwords)
            return false
        } else if (binding.etPassword.text.toString() != binding.etCPassword.text.toString()) {
            error = getString(R.string.invalid_passwords)
            return false
        } else if (binding.etState.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_state)
            return false
        } else if (binding.etDisrict.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_district)
            return false
        } else if (binding.etTaluka.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_taluka)
            return false
        } else if (binding.etVillage.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_village)
            return false
        } else if (binding.etPincode.text.toString().trim().isEmpty()) {
            error = getString(R.string.add_pincode)
            return false
        } else if (binding.etPincode.text.toString().trim().length < 6) {
            error = getString(R.string.invalid_pincode)
            return false
        }
        return true
    }

    private fun callApi() {
        if (isInternetAvailable(this)) {

            val param = HashMap<String, String>()
            param["first_name"] = binding.etFirstName.text.toString().trim()
            param["last_name"] = binding.etLastName.text.toString().trim()
            param["email"] = binding.etEmail.text.toString().trim()
            param["phone_number"] = binding.etPhoneNumber.text.toString().trim()
            param["password"] = binding.etPassword.text.toString().trim()
            param["confirm_password"] = binding.etCPassword.text.toString().trim()
            param["state"] = binding.etState.text.toString().trim()
            param["district"] = binding.etDisrict.text.toString().trim()
            param["taluka"] = binding.etTaluka.text.toString().trim()
            param["village"] = binding.etVillage.text.toString().trim()
            param["pincode"] = binding.etPincode.text.toString().trim()

            SoniApp.mApiCall?.postSignUp(param)?.enqueue(object : Callback<OtpResponseModel> {
                override fun onFailure(call: Call<OtpResponseModel>, t: Throwable) {
                    showToast(getString(R.string.api_error))
                    binding.txtSendOtp.visibility = View.VISIBLE
                    binding.txtSendOtpShimmer.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<OtpResponseModel>, response: Response<OtpResponseModel>?
                ) {
                    if (response != null) {
                        val responseLogin = response.body()
                        if (responseLogin != null) {
                            if (responseLogin.status!!) {
//                                showToast(responseLogin.otp.toString())
                                val intent = Intent(this@SignUpActivity, OtpActivity::class.java)
                                intent.putExtra(
                                    Const.IntentKey.Email, binding.etEmail.text.toString().trim()
                                )
                                intent.putExtra(
                                    Const.IntentKey.PhoneNumber,
                                    binding.etPhoneNumber.text.toString().trim()
                                )
                                intent.putExtra(
                                    "first_name", binding.etFirstName.text.toString().trim()
                                )
                                intent.putExtra(
                                    "last_name", binding.etLastName.text.toString().trim()
                                )
                                intent.putExtra(
                                    "password", binding.etPassword.text.toString().trim()
                                )
                                intent.putExtra(
                                    "confirm_password", binding.etCPassword.text.toString().trim()
                                )
                                intent.putExtra(
                                    "state", binding.etState.text.toString().trim()
                                )
                                intent.putExtra(
                                    "district", binding.etDisrict.text.toString().trim()
                                )
                                intent.putExtra(
                                    "taluka", binding.etTaluka.text.toString().trim()
                                )
                                intent.putExtra(
                                    "village", binding.etVillage.text.toString().trim()
                                )
                                intent.putExtra(
                                    "pincode", binding.etPincode.text.toString().trim()
                                )
                                startActivity(intent)
                                binding.txtSendOtp.visibility = View.VISIBLE
                                binding.txtSendOtpShimmer.visibility = View.GONE
                            } else {
                                showToast(responseLogin.message!!)
                                binding.txtSendOtp.visibility = View.VISIBLE
                                binding.txtSendOtpShimmer.visibility = View.GONE
                            }
                        }
                    }

                }


            })

        } else {
            showToast(getString(R.string.internet_error))
            binding.txtSendOtp.visibility = View.VISIBLE
            binding.txtSendOtpShimmer.visibility = View.GONE

        }
    }

    private fun callStateListApi(
    ) {
        if (isInternetAvailable(this)) {
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"


            SoniApp.mApiCall?.postGeneralStateList(param)
                ?.enqueue(object : Callback<StateListResponse> {
                    override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<StateListResponse>, response: Response<StateListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        stateList.add(
                                            State(
                                                id = -1, name = getString(R.string.state)
                                            )
                                        )
                                        stateList.addAll(responseLogin.data)
                                        stateAdapter.notifyDataSetChanged()
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")

                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")

        }


    }

    private fun callDistrictListApi(
    ) {
        if (isInternetAvailable(this)) {
            binding.districtShimmer.visibility = View.VISIBLE
            binding.llDistrict.visibility = View.INVISIBLE
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"
            param["states_id"] = selectedState.toString()


            SoniApp.mApiCall?.postGeneralDistrictList(param)
                ?.enqueue(object : Callback<DistrictListResponse> {
                    override fun onFailure(call: Call<DistrictListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                        binding.districtShimmer.visibility = View.GONE
                        binding.llDistrict.visibility = View.VISIBLE
                    }

                    override fun onResponse(
                        call: Call<DistrictListResponse>, response: Response<DistrictListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        districtList.addAll(responseLogin.data)
                                        districtAdapter.notifyDataSetChanged()

                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")

                        }
                        binding.districtShimmer.visibility = View.GONE
                        binding.llDistrict.visibility = View.VISIBLE
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.districtShimmer.visibility = View.GONE
            binding.llDistrict.visibility = View.VISIBLE
        }


    }

    private fun callTalukaListApi(
    ) {

        if (isInternetAvailable(this)) {
            binding.talukaShimmer.visibility = View.VISIBLE
            binding.llTaluka.visibility = View.INVISIBLE
            var param: java.util.HashMap<String, String> = java.util.HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"
            param["districts_id"] = selectedDistrict.toString()


            SoniApp.mApiCall?.postGeneralTalukaList(param)
                ?.enqueue(object : Callback<TalukaListResponse> {
                    override fun onFailure(call: Call<TalukaListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<TalukaListResponse>, response: Response<TalukaListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        talukaList.addAll(responseLogin.data)
                                        talukaAdapter.notifyDataSetChanged()

                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                            binding.talukaShimmer.visibility = View.GONE
                            binding.llTaluka.visibility = View.VISIBLE

                        } else {
                            Log.d("State ERROR", "DATA Not Found")
                            binding.talukaShimmer.visibility = View.GONE
                            binding.llTaluka.visibility = View.VISIBLE
                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.talukaShimmer.visibility = View.GONE
            binding.llTaluka.visibility = View.VISIBLE
        }


    }

    fun setStateSpinner() {
        stateAdapter = StateSpinnerAdapter(
            this@SignUpActivity, stateList
        )
        binding.spState.adapter = stateAdapter
        binding.llState.setOnClickListener {
            binding.spState.performClick()
        }
        popup.isAccessible = true
        val popupWindow = popup[binding.spState] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {
                binding.etState.text = stateList[it].name!!
                selectedState = stateList[it].id!!
                districtList.clear()
                districtList.add(
                    District(
                        id = -1, name = getString(R.string.district)
                    )
                )
                districtAdapter.notifyDataSetChanged()
                if(selectedState!=-1) {
                    callDistrictListApi()
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun setDistrictSpinner() {
        districtAdapter = DistrictSpinnerAdapter(
            this@SignUpActivity, districtList
        )
        binding.spDistrict.adapter = districtAdapter
        binding.llDistrict.setOnClickListener {
            if (districtList.size > 1) {
                binding.spDistrict.performClick()
            } else {
                showToast(getString(R.string.please_select_state))
            }
        }
        val popupWindow = popup[binding.spDistrict] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {

                binding.etDisrict.text = districtList[it].name!!
                selectedDistrict = districtList[it].id!!
                talukaList.clear()
                talukaList.add(
                    Taluka(
                        id = -1, name = getString(R.string.taluka)
                    )
                )
                talukaAdapter.notifyDataSetChanged()
                if(selectedDistrict!=-1) {
                    callTalukaListApi()
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun setTalukaSpinner() {
        talukaAdapter = TalukaSpinnerAdapter(
            this@SignUpActivity, talukaList
        )
        binding.spTaluka.adapter = talukaAdapter
        binding.llTaluka.setOnClickListener {
            if (talukaList.size > 1) {
                binding.spTaluka.performClick()
            } else {
                showToast("Please select a district")
            }
        }
        val popupWindow = popup[binding.spTaluka] as androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        binding.spTaluka.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, it: Int, id: Long
            ) {
                binding.etTaluka.text = talukaList[it].name!!
                selectedTaluka = talukaList[it].id!!
//                districtList.clear()
//                districtList.add(
//                    District(
//                        id = -1, name = getString(R.string.market)
//                    )
//                )
//                callDistrictListApi()
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}