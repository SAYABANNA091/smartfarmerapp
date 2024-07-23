package com.soni.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.HomeActivity
import com.soni.activity.InquiryActivity
import com.soni.activity.MapActivity
import com.soni.activity.MarketActivity
import com.soni.activity.WeatherActivity
import com.soni.adapter.InquiriesHomeAdapter
import com.soni.adapter.InquiriesHomeAdapterShimmer
import com.soni.adapter.InquiriesProductsAdapter
import com.soni.databinding.FragmentHomeBinding
import com.soni.services.web.models.HomeInquiryDataResponse
import com.soni.services.web.models.ProductData
import com.soni.services.web.models.PropertyModel
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var inquiriesPropertiesAdapter: InquiriesHomeAdapter
    lateinit var inquiriesProductAdapter: InquiriesProductsAdapter
    var inquiries: ArrayList<PropertyModel> = arrayListOf()
    var inquiriesProducts: ArrayList<ProductData> = arrayListOf()
    var isFromNotification=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  return inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        isFromNotification = bundle!!.getBoolean(Const.IntentKey.isFromNotification,false)
        init()



    }

    override fun onResume() {
        if ((SoniApp.user!!.firstName != null && SoniApp.user!!.firstName.toString().isNotEmpty())) {
            binding.tvUserHello.text =
                "${getString(R.string.hello_user)} ${if (SoniApp.user!!.firstName != null) " " + SoniApp.user!!.firstName else " "}!"
        }else{
            binding.tvUserHello.text =
                "${getString(R.string.hello_user)} !"

        }
        super.onResume()
    }

    private fun init() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        inquiriesPropertiesAdapter = InquiriesHomeAdapter(requireContext(), inquiries)
        binding.rvInquiries.layoutManager = layoutManager
        binding.rvInquiries.adapter = inquiriesPropertiesAdapter

        inquiriesProductAdapter = InquiriesProductsAdapter(requireContext(), inquiriesProducts)
        binding.rvInquiriesPorducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvInquiriesPorducts.adapter = inquiriesProductAdapter



        binding.rvInquiriesShimmer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvInquiriesShimmer.adapter = InquiriesHomeAdapterShimmer(requireContext())

        callInquiryApi()
        onClick()

        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            callInquiryApi()
        })
    }

    private fun onClick() {
        binding.llAttendance.setOnClickListener {
            (requireActivity() as HomeActivity).setAttendanceManagementFragment(
                null,
                Const.TAB.home
            )
        }

        binding.llMyAccount.setOnClickListener {
            (requireActivity() as HomeActivity).setMyAccountFragment(null, Const.TAB.home)
        }

        binding.llRent.setOnClickListener {
            (requireActivity() as HomeActivity).setRentFargment()
        }

        binding.llBuySell.setOnClickListener {
            (requireActivity() as HomeActivity).setBuySellFragment()
        }

        binding.tvInquiryBtn.setOnClickListener {
            val intent = Intent(requireActivity(), InquiryActivity::class.java)
            startActivity(intent)
        }

        binding.llWeather.setOnClickListener {
            val intent = Intent(requireActivity(), WeatherActivity::class.java)
            startActivity(intent)


        }


        binding.llMarket.setOnClickListener {
            if (SoniApp.user!!.state.toString()!="null"&&SoniApp.user!!.state.toString()!="")
            {
                if (SoniApp.user!!.district.toString()!="null"&&SoniApp.user!!.district.toString()!="")
                {
                    if (SoniApp.user!!.taluka.toString()!="null"&&SoniApp.user!!.taluka.toString()!="")
                    {
                        val intent = Intent(requireActivity(), MarketActivity::class.java)
                        startActivity(intent)
                    }else{
                       SoniApp.showInquiryPopup(requireActivity(), arrayListOf(getString((R.string.taluka))))
                    }

                }else{
                    SoniApp.showInquiryPopup(requireActivity(), arrayListOf(getString(R.string.district),getString((R.string.taluka))))
                }

            }else{
                SoniApp.showInquiryPopup(requireActivity(), arrayListOf(getString(R.string.state),getString(R.string.district),getString((R.string.taluka))))
            }

        }

    }

    private fun callInquiryApi() {
        binding.tvInquiryBtn.visibility = View.VISIBLE
//        binding.llInquires.visibility = View.GONE
//        binding.txtInquiries.visibility = View.VISIBLE
//        binding.sfShimmer.visibility = View.VISIBLE
//        binding.tvNoInq.visibility = View.GONE
//
//        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {
//
//            var param: HashMap<String, String> = HashMap()
////            param["Page"] = "1"
////            param["Limit"] = "10"
//
//            SoniApp.mApiCall?.postHomeInquiryLis(param)
//                ?.enqueue(object : Callback<HomeInquiryDataResponse> {
//                    override fun onFailure(call: Call<HomeInquiryDataResponse>, t: Throwable) {
//                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
////                        binding.rvRentItems.visibility = View.VISIBLE
////                        binding.sfShimmer.visibility = View.GONE
//
//                    }
//
//                    override fun onResponse(
//                        call: Call<HomeInquiryDataResponse>,
//                        response: Response<HomeInquiryDataResponse>?
//                    ) {val postDelayed =
//                        Handler(Looper.getMainLooper()).postDelayed(
//                            {
//                                if (response != null) {
//                                    val responseLogin = response.body()
//                                    if (responseLogin != null) {
//                                        if (responseLogin.status!!) {
//                                            if (responseLogin.status!!) {
//                                                inquiries.clear()
//                                                inquiriesProducts.clear()
//
//                                                if (responseLogin.data!!.PropertyInquiry.isNotEmpty()) {
//
//                                                    inquiries.add(responseLogin.data!!.PropertyInquiry[0])
//
//                                                }
//                                                if (responseLogin.data!!.ProductInquiry.isNotEmpty()) {
//
//                                                    inquiriesProducts.add(responseLogin.data!!.ProductInquiry[0])
//
//                                                }
//                                                inquiriesPropertiesAdapter.notifyDataSetChanged()
//                                                inquiriesProductAdapter.notifyDataSetChanged()
////                                                binding.llInquires.visibility = View.VISIBLE
////                                                binding.sfShimmer.visibility = View.GONE
//                                                if (inquiries.isNotEmpty() || inquiriesProducts.isNotEmpty()) {
//                                                    binding.tvInquiryBtn.visibility = View.VISIBLE
////                                                    binding.tvNoInq.visibility = View.VISIBLE
////                                                    binding.llInquires.visibility = View.GONE
//                                                }
//
//                                            }
//                                        } else {
//                                            binding.llInquires.visibility = View.VISIBLE
//                                            binding.sfShimmer.visibility = View.GONE
//                                        }
//
//                                    }
//                                }
//                            },
//                            if(isFromNotification)1000 else 0 // value in milliseconds
//                        )
//
//
//
//                    }
//
//
//                })
//
//        } else {
//            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
//            binding.llInquires.visibility = View.VISIBLE
//            binding.sfShimmer.visibility = View.GONE
//
//        }
    }

}