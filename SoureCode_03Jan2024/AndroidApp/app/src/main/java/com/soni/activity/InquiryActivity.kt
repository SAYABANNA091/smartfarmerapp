package com.soni.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.*
import com.soni.databinding.ActivityInquiryBinding
import com.soni.services.web.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InquiryActivity : BaseActivity() {
    lateinit var binding : ActivityInquiryBinding
    lateinit var inquiriesHomeAdapter : InquiriesHomeAdapter
    var inquiries: ArrayList<PropertyModel> = arrayListOf()

    var inquiriesProducts: ArrayList<ProductData> = arrayListOf()
    lateinit var inquiriesProductAdapter : InquiriesProductsAdapter

    var pageNumber=1
    var totalPages=1
    var pageNumberProducts=1
    var totalPagesProducts=1
    var isLoading=false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    setContentView(R.layout.activity_inquiry)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityInquiryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        ivBack=findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var productsManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        inquiriesHomeAdapter = InquiriesHomeAdapter(this,inquiries)
        binding.rvInquiries.layoutManager = layoutManager
        binding.rvInquiries.adapter = inquiriesHomeAdapter

        ivRight = findViewById(R.id.iv_right)
        ivBack =findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        title = findViewById(R.id.tv_title)
        title.text = getString(R.string.inquiries)
        ivBack.setOnClickListener { this.onBackPressed() }
        binding.tvNoInq.visibility=View.GONE

        binding.rvInquiriesShimmer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvInquiriesShimmer.adapter = InquiriesHomeAdapterShimmer(this,false)

        binding.rvInquiriesProductsShimmer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvInquiriesProductsShimmer.adapter = InquiriesHomeAdapterShimmer(this,true)

        binding.rvInquiries.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {

                    if (layoutManager.findLastCompletelyVisibleItemPosition() >= inquiriesHomeAdapter.itemCount - 1)
                    {
                        // call data
                        loadmore()
                        isLoading=true

                    }
                }
            }
        })
        binding.rvInquiriesPorducts.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {

                    if (productsManager.findLastCompletelyVisibleItemPosition() >= inquiriesProductAdapter.itemCount - 1)
                    {
                        // call data
                        loadmore()
                        isLoading=true

                    }
                }
            }
        })
        callPropertiesApi(false)

       binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
           binding.swipeRefresh.isRefreshing = false
           if (binding.tabLayout.selectedTabPosition == 0) {
               pageNumber = 1
               callPropertiesApi(false)
           } else {
               pageNumberProducts = 1
               callProductApi(false)
           }
        })
        inquiriesProductAdapter = InquiriesProductsAdapter(this,inquiriesProducts)
        binding.rvInquiriesPorducts.layoutManager =  productsManager
        binding.rvInquiriesPorducts.adapter = inquiriesProductAdapter

       var propertiesTab = binding.tabLayout.newTab()
        propertiesTab.id = 0
        propertiesTab.text = "Properties"
       var ProductsTab = binding.tabLayout.newTab()
        ProductsTab.id = 1
        ProductsTab.text = "Products"

        binding.tabLayout.addTab(propertiesTab)
        binding.tabLayout.addTab(ProductsTab)


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("TAB ID", tab!!.id.toString())
                when (tab.id) {
                    0 -> {
                        binding.rvInquiries.visibility = View.VISIBLE
                        binding.tvNoInq .visibility = View.GONE
                        binding.rvInquiriesPorducts.visibility = View.GONE
                        binding.rlInquiry.visibility=View.VISIBLE
                        binding.rlInquiryProducts.visibility=View.GONE

                        if (inquiries.isEmpty()) {
                            binding.rvInquiries.visibility = View.GONE
                            binding.sfShimmer.visibility = View.VISIBLE
                            callPropertiesApi(isPage=false)
                        }

                    }
                    1 -> {
                        binding.rvInquiries.visibility = View.GONE
                        binding.tvNoInq.visibility=View.GONE
                        binding.rvInquiriesPorducts.visibility = View.VISIBLE
                        binding.rlInquiry.visibility=View.GONE
                        binding.rlInquiryProducts.visibility=View.VISIBLE

                        if (inquiriesProducts.isEmpty()) {
                            binding.rvInquiriesPorducts.visibility = View.GONE
                            binding.sfShimmerProducts.visibility = View.VISIBLE
                            callProductApi(isPage=false)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    private fun loadmore(){
        if (binding.tabLayout.selectedTabPosition == 0) {
            if(pageNumber< totalPages!!){
                pageNumber++
                callPropertiesApi(true)

            }
        } else {
            if(pageNumberProducts< totalPagesProducts!!){
                pageNumberProducts++
                callProductApi(true)

            }
        }

    }


    private fun callPropertiesApi(isPage:Boolean) {
        if (!isPage) {
            binding.rvInquiries.visibility = View.GONE
            binding.sfShimmer.visibility = View.VISIBLE
        }
        if (isInternetAvailable(this)) {
            isLoading=true
            var param: HashMap<String, String> = HashMap()
            param["Page"] = pageNumber.toString()
            param["Limit"] = "20"

            SoniApp.mApiCall?.postInquiryList(param)
                ?.enqueue(object : Callback<InquiryListResponse> {
                    override fun onFailure(call: Call<InquiryListResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
//                        binding.rvRentItems.visibility = View.VISIBLE
//                        binding.sfShimmer.visibility = View.GONE
                        isLoading=false

                    }

                    override fun onResponse(
                        call: Call<InquiryListResponse>,
                        response: Response<InquiryListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                      if (!isPage){
                                          inquiries.clear()
                                      }
                                        totalPages=responseLogin.totalpage!!
                                        inquiries.addAll(responseLogin.data)
                                        inquiriesHomeAdapter.notifyDataSetChanged()
                                        binding.rvInquiries.visibility = View.VISIBLE
                                        binding.sfShimmer.visibility = View.GONE
                                        isLoading=false

                                    }
                                } else {
                                    binding.rvInquiries.visibility = View.VISIBLE
                                    binding.sfShimmer.visibility = View.GONE
                                    isLoading=false
                                }

                                if(inquiries.isEmpty()){
                                    binding.tvNoInq.visibility=View.VISIBLE
                                    isLoading=false
                                }
                            }
                        }

                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvInquiries.visibility = View.VISIBLE
            binding.sfShimmer.visibility = View.GONE
            isLoading=false
        }
    }

    private fun callProductApi(isPage:Boolean) {
        binding.tvNoInq.visibility=View.GONE
        if (!isPage) {
            binding.rvInquiriesPorducts.visibility = View.GONE
            binding.sfShimmerProducts.visibility = View.VISIBLE
        }
        if (isInternetAvailable(this)) {
            isLoading=false
            var param: HashMap<String, String> = HashMap()
            param["Page"] = pageNumberProducts.toString()
            param["Limit"] = "5"

            SoniApp.mApiCall?.postProductInquiryList(param)
                ?.enqueue(object : Callback<GetProductListResponse> {
                    override fun onFailure(call: Call<GetProductListResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
//                        binding.rvRentItems.visibility = View.VISIBLE
//                        binding.sfShimmer.visibility = View.GONE
                        isLoading=false
                    }

                    override fun onResponse(
                        call: Call<GetProductListResponse>,
                        response: Response<GetProductListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                      if (!isPage){
                                          inquiriesProducts.clear()
                                      }
                                        inquiriesProducts.addAll(responseLogin.data)
                                        inquiriesProductAdapter.notifyDataSetChanged()
                                        binding.rvInquiriesPorducts.visibility = View.VISIBLE
                                        binding.sfShimmerProducts.visibility = View.GONE
                                        totalPagesProducts=if(responseLogin.totalpage !=null) responseLogin.totalpage!! else 1
                                        isLoading=false
                                    }
                                } else {
                                    binding.rvInquiriesPorducts.visibility = View.VISIBLE
                                    binding.sfShimmerProducts.visibility = View.GONE
                                    isLoading=false
                                }
                                if(inquiriesProducts.isEmpty()){
                                    binding.tvNoInq.visibility=View.VISIBLE
                                    isLoading=false
                                }
                            }
                        }

                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvInquiriesPorducts.visibility = View.VISIBLE
            binding.sfShimmerProducts.visibility = View.GONE
            isLoading=false
        }
    }

}