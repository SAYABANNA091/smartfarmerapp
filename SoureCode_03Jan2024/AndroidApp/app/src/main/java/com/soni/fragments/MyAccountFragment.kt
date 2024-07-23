package com.soni.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soni.Preference.StoreUserData
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.AddEditProductsActivity
import com.soni.activity.HomeActivity
import com.soni.adapter.InquiriesHomeAdapter
import com.soni.adapter.UserAccountListAdapter
import com.soni.adapter.UserAccountListAdapterShimmer
import com.soni.databinding.FragmentMyAccountBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.UserPopertiesModel
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body


class MyAccountFragment : BaseFragment() {

    lateinit var userAccountListAdapter: UserAccountListAdapter
    lateinit var binding: FragmentMyAccountBinding
    var list:ArrayList<PropertyModel> = ArrayList()

    var pageNumber=1
    var totalPages=1
    var isLoading=false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_my_account, container, false)

        binding = FragmentMyAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
    }

    override fun onResume() {
        super.onResume()
        pageNumber=1
        callApi(false)
    }
    private fun init() {

        ivBack = requireView().findViewById(R.id.iv_back)
        ivRight = requireView().findViewById(R.id.iv_right)
        tvTitle = requireView().findViewById(R.id.tv_title)

        ivBack.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }

        tvTitle.text = requireContext().getString(R.string.my_account)

        ivRight.setOnClickListener {
            if (SoniApp.user!!.firstName!=null&&SoniApp.user!!.firstName!=""){
                if (SoniApp.user!!.pincode!=null&&SoniApp.user!!.pincode!=""){
                    val bundle = Bundle()
                    bundle.putInt("screenType", 2)
                    (requireActivity() as HomeActivity).seEditPropertiesFragment(bundle, Const.TAB.home)
                }else{
                    SoniApp.showInquiryPopup(requireActivity(), arrayListOf(getString(R.string.your_pincode)))
                }
            }else{
                SoniApp.showInquiryPopup(requireActivity(), arrayListOf(getString(R.string.your_first_name),getString(R.string.your_pincode)))

            }

        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var userAccountListAdapter = UserAccountListAdapterShimmer(requireContext(), ArrayList())
        binding.rvShimmer.layoutManager = layoutManager
        binding.rvShimmer.adapter = userAccountListAdapter
        binding.rvUserAccountList.visibility = View.GONE
        binding.llShimmer.visibility = View.VISIBLE
        setAdapter()
        callApi(false)


    }

    private fun setAdapter() {
        if (isAdded) {
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            userAccountListAdapter = UserAccountListAdapter(requireContext(), list) { it ->
                showLanguageDialog(it)
            }
            if(list.isEmpty()) {
                binding.llEmptySearch.visibility = View.VISIBLE
            }else{
                binding.rvUserAccountList.visibility = View.VISIBLE
            }
            binding.rvUserAccountList.layoutManager = layoutManager
            binding.rvUserAccountList.adapter = userAccountListAdapter

            binding.llShimmer.visibility = View.GONE
            binding.rvUserAccountList.addOnScrollListener(object : RecyclerView.OnScrollListener()
            {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!isLoading)
                    {

                        if (layoutManager.findLastCompletelyVisibleItemPosition() >= userAccountListAdapter.itemCount - 5)
                        {
                            // call data
                            loadmore()
                            isLoading=true

                        }
                    }
                }
            })
        }
    }
    private fun loadmore(){
        if(pageNumber< totalPages!!){
            pageNumber++
            callApi(true)

        }
    }
    private fun callApi(isPage:Boolean) {
        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {
            binding.llEmptySearch.visibility = View.GONE
         if(!isPage){

             binding.rvUserAccountList.visibility = View.GONE
             binding.llShimmer.visibility = View.VISIBLE
         }
            var param: HashMap<String, String> = HashMap()
            param["Page"] = pageNumber.toString()
            param["Limit"] = "10"

            SoniApp.mApiCall?.postGetMyProperty(param)
                ?.enqueue(object : Callback<UserPopertiesModel> {
                    override fun onFailure(call: Call<UserPopertiesModel>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.rvUserAccountList.visibility = View.GONE
                        binding.llShimmer.visibility = View.GONE
                        isLoading=false
                    }

                    override fun onResponse(
                        call: Call<UserPopertiesModel>,
                        response: Response<UserPopertiesModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        if (!isPage){
                                            list.clear()
                                        }
                                        list.addAll(responseLogin.data)
                                        totalPages=responseLogin.totalpage!!
                                        userAccountListAdapter.onUpdate()
                                        val postDelayed =
                                            Handler(Looper.getMainLooper()).postDelayed(
                                                {
                                                    binding.rvUserAccountList.visibility = View.VISIBLE
                                                    binding.llShimmer.visibility = View.GONE
                                                    if(list.isEmpty()){
                                                        binding.llEmptySearch.visibility=View.VISIBLE
                                                    }
                                                },
                                                1000 // value in milliseconds
                                            )

                                    }
                                } else {
                                    (requireActivity() as HomeActivity).showToast(responseLogin.message!!)

                                }
                            }
                            binding.rvUserAccountList.visibility = View.VISIBLE
                            binding.llShimmer.visibility = View.GONE
                            isLoading=false
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
            binding.rvUserAccountList.visibility = View.GONE
            binding.llShimmer.visibility = View.GONE
            isLoading=false
        }
    }

    private fun callDeleteApi(id:Int) {

        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

            var param: HashMap<String, String> = HashMap()
            param["id"] = list[id].id!!.toString()

            SoniApp.mApiCall?.postDeleteProperty(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.rvUserAccountList.visibility = View.GONE
                        binding.llShimmer.visibility = View.GONE

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
                                        userAccountListAdapter.onDelete(id)
                                        if(userAccountListAdapter.list.isEmpty()){
                                            binding.llEmptySearch.visibility = View.VISIBLE
                                        }
                                    }
                                } else {

                                }
                                (requireActivity() as HomeActivity).showToast(responseLogin.message!!)

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
            binding.rvUserAccountList.visibility = View.GONE
            binding.llShimmer.visibility = View.GONE
        }
    }

    private fun showLanguageDialog(id:Int){

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
        txtyes.setOnClickListener {
            dialog.dismiss()

            callDeleteApi(id)
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }


}