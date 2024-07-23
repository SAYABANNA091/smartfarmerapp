package com.soni.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.*
import com.soni.databinding.ActivityMarketBinding
import com.soni.services.web.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field

class MarketActivity : BaseActivity() {
    lateinit var binding: ActivityMarketBinding
    lateinit var marketListAdapter: MarketListAdapter
    lateinit var marketListLayoutManager: LinearLayoutManager
    lateinit var stateAdapter: StateSpinnerAdapter
    lateinit var marketAdapter: MarketSpinnerAdapter
    lateinit var commodityAdapter: CommoditySpinnerAdapter
    var selectedState = 0
    var selectedMarket = 0
    var selectedCommodity = 0
    var districtList: ArrayList<District> = arrayListOf()
    var commodityList: ArrayList<Commodity> = arrayListOf()
    var marketDataList: ArrayList<MarketData> = arrayListOf()
    var pageNumber = 1
    var totalPages = 1
    var isLoading = false
    var isFirstCall = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
        districtList.add(District(id = -1, name = getString(R.string.market)))
        commodityList.add(Commodity(id = -1, name = getString(R.string.commodity)))
        init()
    }

    private fun init() {
        ivBack = findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }


        ivRight = findViewById(R.id.iv_right)
        ivBack = findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        title = findViewById(R.id.tv_title)
        title.text = getString(R.string.market)
        ivBack.setOnClickListener { this.onBackPressed() }

        marketListLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        marketListAdapter = MarketListAdapter(this, marketDataList)
        binding.rvMarket.layoutManager = marketListLayoutManager
        binding.rvMarket.adapter = marketListAdapter

        binding.rvMarketShimmer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMarketShimmer.adapter = BuySelItemListAdapterShimmer(this, true)

        binding.root.setOnClickListener {
            hideKeyboard(binding.root)
            binding.etSearch.clearFocus()
        }
        binding.ivClearSearch.setOnClickListener {
           if (!isLoading){
               binding.etSearch.setText("")
               pageNumber=1
               if (!isFirstCall) {
                   callDataListApi(isPage = false)
               }
               hideKeyboard(binding.root)
           }
        }
        binding.etSearch.setOnFocusChangeListener { view, b ->
            onFocusChange(binding.llSearch, b)
        }
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if(!isLoading){
                hideKeyboard(binding.root)
                if (!isFirstCall) {
                    callDataListApi(isPage = false, isSearch = true)
                }
            }
            true
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            pageNumber = 1
            if (!isFirstCall) {
                callDataListApi(false)
            }
        }
        binding.rvMarket.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {

                    if (marketListLayoutManager.findLastCompletelyVisibleItemPosition() >= marketListAdapter.itemCount - 20) {
                        // call data

                        if (pageNumber < totalPages) {
                            pageNumber++
                            if (!isFirstCall) {
                                callDataListApi(true)
                            }

                        }


                    }
                }
            }
        })
        stateAdapter = StateSpinnerAdapter(this, SoniApp.stateList)
        binding.spLoc.adapter = stateAdapter
        binding.spLoc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, it: Int, id: Long) {
                    if (!isFirstCall)
                    {
                        binding.tvState.text = SoniApp.stateList[it].name!!
                        selectedState = SoniApp.stateList[it].id!!
                        districtList.clear()
                        districtList.add(District(id = -1, name = getString(R.string.market)))
                        binding.tvMarket.text = districtList[0].name!!
                        selectedMarket = 0
                        if (it != 0) {
                            callMarketListApi()
                        }
                        pageNumber = 1
                        callDataListApi(false)

                    }            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.llSpState.setOnClickListener {
            if(!isLoading) {
                binding.spLoc.performClick()
            }
        }
        val popup: Field =
            AppCompatSpinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow = popup[binding.spLoc] as
                androidx.appcompat.widget.ListPopupWindow
        popupWindow.height =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()


        marketAdapter = MarketSpinnerAdapter(this, districtList)
        binding.spMarket.adapter = marketAdapter
        binding.spMarket.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, it: Int, id: Long) {
                binding.tvMarket.text = districtList[it].name!!
                selectedMarket = districtList[it].id!!
                pageNumber = 1

                if (!isFirstCall) {
                    callDataListApi(false)
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.llSpMarket.setOnClickListener {
            if (districtList.size > 1 && !isLoading) {
                binding.spMarket.performClick()
            }
        }
        val popupMarket: Field =
            AppCompatSpinner::class.java.getDeclaredField("mPopup")
        popupMarket.isAccessible = true
        val popupMarketWindow = popupMarket[binding.spMarket] as
                androidx.appcompat.widget.ListPopupWindow
        popupMarketWindow.height =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        popupMarketWindow.width =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp).toInt()

        commodityAdapter = CommoditySpinnerAdapter(this, commodityList)
        binding.spCommodity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, it: Int, id: Long) {
                binding.tvCommodity.text = commodityList[it].name!!
                selectedCommodity = commodityList[it].id!!
                pageNumber = 1
                if (!isFirstCall) {
                    callDataListApi(false)
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        binding.spCommodity.adapter = commodityAdapter
        binding.llSpCommodity.setOnClickListener {
            if (commodityList.size > 1 && !isLoading) {
                binding.spCommodity.performClick()
            }
        }

        val popupCommodity: Field =
            AppCompatSpinner::class.java.getDeclaredField("mPopup")
        popupCommodity.isAccessible = true
        val popupCommodityWindow = popupCommodity[binding.spCommodity] as
                androidx.appcompat.widget.ListPopupWindow
        popupCommodityWindow.height =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()
        popupCommodityWindow.width =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp).toInt()


        callCommodityListApi()


        for(i in 0 until  SoniApp.stateList.size) {
            if(SoniApp.stateList[i].name.equals(SoniApp.user!!.state!!,true)) {
                binding.tvState.text = SoniApp.stateList[i].name!!
                selectedState = SoniApp.stateList[i].id!!
                districtList.clear()
                districtList.add(District(id = -1, name = getString(R.string.market)))
                binding.tvMarket.text = districtList[0].name!!
                selectedMarket = 0
                if (i != 0) {
                    callMarketListApi()
                }
                pageNumber = 1
                callDataListApi(false)
            }}
    }

    private fun callMarketListApi(
    ) {
        if (isInternetAvailable(this)) {
            var param: HashMap<String, String> = HashMap()

            param["Limit"] = "100"
            param["Page"] = "1"
            param["stateName"] = if (selectedState == 0) "" else binding.tvState.text.toString()


            SoniApp.mApiCall?.postGetMarketListList(param)
                ?.enqueue(object : Callback<DistrictListResponse> {
                    override fun onFailure(call: Call<DistrictListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))

                    }

                    override fun onResponse(
                        call: Call<DistrictListResponse>,
                        response: Response<DistrictListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {

                                        districtList.addAll(responseLogin.data)
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

    private fun callCommodityListApi(
    ) {
        if (isInternetAvailable(this)) {
            var param: HashMap<String, String> = HashMap()

            param["Limit"] = "10000000000"
            param["Page"] = "1"


            SoniApp.mApiCall?.postGetCommodityList(param)
                ?.enqueue(object : Callback<CommodityListResponse> {
                    override fun onFailure(call: Call<CommodityListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<CommodityListResponse>,
                        response: Response<CommodityListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {

                                        commodityList.addAll(responseLogin.data)
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

    private fun callDataListApi(
        isPage: Boolean,
        isSearch: Boolean = false
    ) {
        if (isInternetAvailable(this)) {
            binding.llEmptySearch.visibility=View.GONE
            isLoading = true
            if (!isPage) {
                marketDataList.clear()
                marketListAdapter.notifyDataSetChanged()
                binding.sfShimmer.visibility = View.VISIBLE
                binding.rvMarket.visibility = View.GONE
            }
            var param: HashMap<String, String> = HashMap()

            param["Limit"] = "50"
            param["Page"] = pageNumber.toString()
            if (selectedState > 0) {
                param["state"] = binding.tvState.text.toString()
            }
            if (selectedMarket > 0) {
                param["market"] = binding.tvMarket.text.toString()
            }
          if(!isSearch){
              if (selectedCommodity > 0) {
                  param["commodity"] = binding.tvCommodity.text.toString()
              }
          }
            param["search_text"] = binding.etSearch.text.toString()


            SoniApp.mApiCall?.postMarketDataList(param)
                ?.enqueue(object : Callback<MarketDataListResponse> {
                    override fun onFailure(call: Call<MarketDataListResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.sfShimmer.visibility = View.GONE
                        binding.rvMarket.visibility = View.VISIBLE

                        isLoading = false
                    }


                    override fun onResponse(
                        call: Call<MarketDataListResponse>,
                        response: Response<MarketDataListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        isFirstCall = false
                                        marketDataList.addAll(responseLogin.data)
                                        marketListAdapter.notifyDataSetChanged()
                                        binding.sfShimmer.visibility = View.GONE
                                        binding.rvMarket.visibility = View.VISIBLE
                                        totalPages =
                                            if (responseLogin.totalpage != null) responseLogin.totalpage!! else 1
                                    if (marketDataList.isEmpty()){
                                        binding.llEmptySearch.visibility=View.VISIBLE
                                        binding.rvMarket.visibility = View.GONE
                                        binding.sfShimmer.visibility = View.GONE
                                    }
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")
                                    binding.sfShimmer.visibility = View.GONE
                                    binding.rvMarket.visibility = View.VISIBLE
                                    if (marketDataList.isEmpty()){
                                        binding.llEmptySearch.visibility=View.VISIBLE
                                        binding.rvMarket.visibility = View.GONE
                                        binding.sfShimmer.visibility = View.GONE
                                    }
                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")
                            binding.sfShimmer.visibility = View.GONE
                            binding.rvMarket.visibility = View.VISIBLE
                            if (marketDataList.isEmpty()){
                                binding.llEmptySearch.visibility=View.VISIBLE
                                binding.rvMarket.visibility = View.GONE
                                binding.sfShimmer.visibility = View.GONE
                            }
                        }
                        isLoading = false
                        if (marketDataList.isEmpty()){
                            binding.llEmptySearch.visibility=View.VISIBLE
                            binding.rvMarket.visibility = View.GONE
                            binding.sfShimmer.visibility = View.GONE
                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.sfShimmer.visibility = View.GONE
            binding.rvMarket.visibility = View.VISIBLE
            isLoading = false
            if (marketDataList.isEmpty()){
                binding.llEmptySearch.visibility=View.VISIBLE
                binding.rvMarket.visibility = View.GONE
                binding.sfShimmer.visibility = View.GONE
            }
        }


    }

    override fun onPause() {
        hideKeyboard(binding.root)
        super.onPause()
    }
}