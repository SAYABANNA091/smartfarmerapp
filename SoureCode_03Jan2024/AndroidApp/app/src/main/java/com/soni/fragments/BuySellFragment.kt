package com.soni.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.AddEditProductsActivity
import com.soni.activity.BaseActivity
import com.soni.activity.FavouriteProductsActivity
import com.soni.activity.HomeActivity
import com.soni.adapter.BuySelItemListAdapter
import com.soni.adapter.BuySelItemListAdapterShimmer
import com.soni.adapter.ProductTypeAdapter
import com.soni.databinding.FragmentBuySellBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.GetProductListResponse
import com.soni.services.web.models.ProductData
import com.soni.utils.LocationCallbackListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuySellFragment : BaseFragment(), LocationCallbackListener {

    lateinit var binding: FragmentBuySellBinding
    lateinit var buyAdapter: BuySelItemListAdapter
    lateinit var sellAdapter: BuySelItemListAdapter
    lateinit var buyShimmerAdapter: BuySelItemListAdapterShimmer
    lateinit var sellShimmerAdapter: BuySelItemListAdapterShimmer
    lateinit var productTypeAdapter: ProductTypeAdapter

    lateinit var buyTab: TabLayout.Tab
    lateinit var sellTab: TabLayout.Tab
    var buyPageNumber = 1
    var buyTotalPage = 1
    var buyList: ArrayList<ProductData> = arrayListOf()

    var sellPageNumber = 1
    var sellTotalPage = 1
    var sellList: ArrayList<ProductData> = arrayListOf()
    var selectedCategory: Int = 1
    var isLoading = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //  return inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentBuySellBinding.inflate(layoutInflater)
        HomeActivity.locationCall.add(this@BuySellFragment)

        selectedCategory = SoniApp.productType[0].id!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()

    }

    override fun onResume() {
        binding.tvUserHello.text =
            "${getString(R.string.hello_user)} ${if (SoniApp.user!!.firstName != null) " " + SoniApp.user!!.firstName else " "}!"


        if (SoniApp.location != null) {
            if (binding.tabLayoutBuySell.selectedTabPosition == 0) {
                callApi(isBuy = true, isSearch = false, isPage = false)
            } else {
                callApi(isBuy = false, isSearch = false, isPage = false)

            }
        } else {
            HomeActivity.activity!!.getLocation()
        }
        super.onResume()
    }

    private fun init() {
//        (requireActivity()as HomeActivity).getLocation()

        buyAdapter =
            BuySelItemListAdapter(requireActivity(), true, buyList) { pos: Int, isFav: Int ->
                callApiFav(id = buyList[pos].id!!.toString(), isFav = isFav.toString())
            }
        sellAdapter =
            BuySelItemListAdapter(requireActivity(), false, sellList) { pos: Int, isFav: Int ->

            }


        buyShimmerAdapter = BuySelItemListAdapterShimmer(requireActivity())
        sellShimmerAdapter = BuySelItemListAdapterShimmer(requireActivity())

        productTypeAdapter = ProductTypeAdapter(requireActivity()) {
            buyPageNumber = 1
            buyTotalPage = 1
            selectedCategory = it
            buyList.clear()
            callApi(
                isBuy = true,
                isSearch = binding.etSearch.text.toString().trim().isNotEmpty(),
                isPage = false
            )
        }

        binding.rvBuyItems.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvBuyItems.adapter = buyAdapter

        binding.rvBuyItemsShimmer.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvBuyItemsShimmer.adapter = buyShimmerAdapter
        binding.rvSellItemsShimmer.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSellItemsShimmer.adapter = sellShimmerAdapter

        binding.rvSellItems.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSellItems.adapter = sellAdapter


        binding.rvTypes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTypes.adapter = productTypeAdapter


        buyTab = binding.tabLayoutBuySell.newTab()
        buyTab.id = 0
        buyTab.text = getString(R.string.buy)
        sellTab = binding.tabLayoutBuySell.newTab()
        sellTab.id = 1
        sellTab.text = getString(R.string.sell)

        binding.tabLayoutBuySell.addTab(buyTab)
        binding.tabLayoutBuySell.addTab(sellTab)

        binding.ivAddProduct.setOnClickListener {
            moveToAddProduct()

        }
        binding.fab.setOnClickListener {
            moveToAddProduct()
        }
        binding.ivAddProduct.visibility = View.GONE
        binding.tabLayoutBuySell.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("TAB ID", tab!!.id.toString())
                when (tab.id) {
                    0 -> {
                        binding.llBuyTabItems.visibility = View.VISIBLE
                        binding.llBuyEmptySearch.visibility = View.GONE
                        binding.llSellTabItems.visibility = View.GONE
                        binding.ivAddProduct.visibility = View.GONE
                        if (buyList.isEmpty()) {
                            binding.rvBuyItems.visibility = View.GONE
                            binding.sfBuyShimmer.visibility = View.VISIBLE
                            callApi(isBuy = true, isSearch = false, isPage = false)
                        }

                    }

                    1 -> {
                        binding.llSellEmptySearch.visibility = View.GONE
                        binding.llBuyTabItems.visibility = View.GONE
                        binding.llSellTabItems.visibility = View.VISIBLE
                        binding.ivAddProduct.visibility = View.VISIBLE
                        if (sellList.isEmpty()) {
                            binding.rvSellItems.visibility = View.GONE
                            binding.sfSellShimmer.visibility = View.VISIBLE
                            callApi(isBuy = false, isSearch = false, isPage = false)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.root.setOnClickListener {
            requireActivity().hideKeyboard(binding.root)
            binding.etSearch.clearFocus()
        }
        binding.etSearch.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llSearch, b)
        }
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            (requireActivity() as BaseActivity).hideKeyboard(binding.root)
            if (binding.tabLayoutBuySell.selectedTabPosition == 0) {
                selectedCategory = -1
                productTypeAdapter.seletedPos = -1
                productTypeAdapter.notifyDataSetChanged()
            }


            binding.etSearch.clearFocus()
            callApi(
                isBuy = binding.tabLayoutBuySell.selectedTabPosition == 0,
                isSearch = true,
                isPage = false
            )
            true
        }
        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
            if (binding.tabLayoutBuySell.selectedTabPosition == 0) {
                binding.rvTypes.getChildAt(0).performClick()
            }
            (requireActivity() as BaseActivity).hideKeyboard(binding.root)
            binding.etSearch.clearFocus()
            callApi(
                isBuy = binding.tabLayoutBuySell.selectedTabPosition == 0,
                isSearch = false,
                isPage = false
            )
        }

        binding.llBuyTabItems.visibility = View.VISIBLE
        binding.rvBuyItems.visibility = View.GONE
        binding.sfBuyShimmer.visibility = View.VISIBLE
        binding.rvSellItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (true) {
                        val layoutManager = binding.rvSellItems.layoutManager as GridLayoutManager
                        val visibleItemCount =
                            layoutManager.findLastCompletelyVisibleItemPosition() + 1
                        if (visibleItemCount >= layoutManager.itemCount - 5) {
                            if (sellPageNumber < sellTotalPage) {
                                sellPageNumber++
                                isLoading = true
                                callApi(isBuy = false, isSearch = false, isPage = true)
                            }
                        }


                    }
                }
            }
        })
        binding.rvBuyItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (true) {
                        val layoutManager = binding.rvBuyItems.layoutManager as GridLayoutManager
                        val visibleItemCount =
                            layoutManager.findLastCompletelyVisibleItemPosition() + 1
                        if (visibleItemCount >= layoutManager.itemCount - 5) {
                            if (buyPageNumber < buyTotalPage) {
                                buyPageNumber++
                                isLoading = true
                                callApi(isBuy = true, isSearch = false, isPage = true)
                            }
                        }


                    }
                }
            }
        })

        binding.ivFavIcon.setOnClickListener {
            var mIntent = Intent(requireContext(), FavouriteProductsActivity::class.java)
            requireContext().startActivity(mIntent)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            binding.etSearch.setText("")
            (requireActivity() as BaseActivity).hideKeyboard(binding.root)
            binding.etSearch.clearFocus()
            if (binding.tabLayoutBuySell.selectedTabPosition == 0) {
                buyPageNumber = 1
            } else {
                sellPageNumber = 1
            }
            callApi(
                isBuy = binding.tabLayoutBuySell.selectedTabPosition == 0,
                isSearch = false,
                isPage = false
            )
        }


    }

    private fun callApi(isBuy: Boolean, isSearch: Boolean, isPage: Boolean) {
        var param = HashMap<String, String>()
        if (SoniApp.location != null) {

        } else {
            HomeActivity.activity!!.getLocation()
            return
        }
        try {
            if (isBuy) {
                if (!isPage) {
                    binding.sfBuyShimmer.visibility = View.VISIBLE
                    binding.rvBuyItems.visibility = View.GONE
                    buyList.clear()
                    buyAdapter.notifyDataSetChanged()
                    binding.rvBuyItems.smoothScrollToPosition(0)

                }
                binding.llBuyEmptySearch.visibility = View.GONE
                binding.llBuyTabItems.visibility = View.VISIBLE
                if (isSearch) {
                    buyPageNumber = 1
                }
                param.put("come_from", "buy")
                param.put("u_pincode", SoniApp.user!!.pincode ?: "000000")
                param.put("Page", buyPageNumber.toString())
                if (buyPageNumber == 1) {
                    buyList.clear()
                }

            } else {
                binding.llSellEmptySearch.visibility = View.GONE
                binding.llSellTabItems.visibility = View.VISIBLE
                if (!isPage) {
                    binding.sfSellShimmer.visibility = View.VISIBLE
                    binding.rvSellItems.visibility = View.GONE
                }

                if (isSearch) {
                    sellPageNumber = 1
                }
                param.put("Page", sellPageNumber.toString())
                if (sellPageNumber == 1) {
                    sellList.clear()
                }
            }
            param.put("Limit", "10")
            param["Latitude"] = SoniApp.location!!.latitude.toString()
            param["Longitude"] = SoniApp.location!!.longitude.toString()


            if (selectedCategory != -1 && isBuy) {
                param.put("category_id", selectedCategory.toString())
            }

            if (isSearch) param.put("product_title", binding.etSearch.text.toString().trim())





            if ((requireActivity() as BaseActivity).isInternetAvailable(requireActivity())) {

                SoniApp.mApiCall?.postGetProductsList(param)
                    ?.enqueue(object : Callback<GetProductListResponse> {
                        override fun onFailure(call: Call<GetProductListResponse>, t: Throwable) {
                            (requireActivity() as BaseActivity).showToast(getString(R.string.api_error))
                            if (isBuy) {
                                binding.rvBuyItems.visibility = View.VISIBLE
                                binding.sfBuyShimmer.visibility = View.GONE
                                if (buyList.isEmpty()) {
                                    binding.llBuyEmptySearch.visibility = View.VISIBLE
                                }
                                buyAdapter.notifyDataSetChanged()
                            } else {
                                binding.rvSellItems.visibility = View.VISIBLE
                                binding.sfSellShimmer.visibility = View.GONE
                                if (sellList.isEmpty()) {
                                    binding.llSellEmptySearch.visibility = View.VISIBLE
                                }
                                sellAdapter.notifyDataSetChanged()
                            }
                            isLoading = false
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
                                            if (isBuy) {
                                                if (responseLogin.data.isNotEmpty()) {
                                                    if (responseLogin.data[0].categoryId.toString() == selectedCategory.toString()) {

                                                        buyList.addAll(responseLogin.data)
                                                        binding.rvBuyItems.visibility = View.VISIBLE
                                                        binding.sfBuyShimmer.visibility = View.GONE
                                                        buyTotalPage = responseLogin.totalpage!!
                                                        if (buyList.isEmpty()) {

                                                            binding.sfBuyShimmer.visibility =
                                                                View.GONE
                                                            binding.llBuyEmptySearch.visibility =
                                                                View.VISIBLE
                                                        }
                                                        buyAdapter.notifyDataSetChanged()

                                                    }
                                                } else {
                                                    if (buyList.isEmpty()) {
                                                        binding.sfBuyShimmer.visibility = View.GONE
                                                        binding.llBuyEmptySearch.visibility =
                                                            View.VISIBLE
                                                    }
                                                }
                                            } else {
                                                if (responseLogin.data.isNotEmpty()) {
                                                    sellList.addAll(responseLogin.data)
                                                    sellTotalPage = responseLogin.totalpage!!
                                                    binding.rvSellItems.visibility = View.VISIBLE
                                                    binding.sfSellShimmer.visibility = View.GONE

                                                    if (sellList.isEmpty()) {

                                                        binding.sfSellShimmer.visibility = View.GONE
                                                        binding.llSellEmptySearch.visibility =
                                                            View.VISIBLE
                                                    }
                                                    sellAdapter.notifyDataSetChanged()
                                                } else {
                                                    if (sellList.isEmpty()) {

                                                        binding.sfSellShimmer.visibility = View.GONE
                                                        binding.llSellEmptySearch.visibility =
                                                            View.VISIBLE
                                                    }
                                                }
                                            }
                                        }
                                    } else {

                                        (requireActivity() as BaseActivity).showToast(responseLogin.message!!)
                                        if (isBuy) {
                                            binding.rvBuyItems.visibility = View.VISIBLE
                                            binding.sfBuyShimmer.visibility = View.GONE
                                            if (buyList.isEmpty()) {
                                                binding.llBuyEmptySearch.visibility = View.VISIBLE
                                            }
                                            buyAdapter.notifyDataSetChanged()
                                        } else {
                                            binding.rvSellItems.visibility = View.VISIBLE
                                            binding.sfSellShimmer.visibility = View.GONE
                                            if (sellList.isEmpty()) {
                                                binding.llSellEmptySearch.visibility = View.VISIBLE
                                            }
                                            sellAdapter.notifyDataSetChanged()
                                        }


                                    }
                                }
                            }

                            isLoading = false
                        }
                    })

            } else {
                isLoading = false
                (requireActivity() as BaseActivity).showToast(getString(R.string.internet_error))
                if (isBuy) {
                    binding.rvBuyItems.visibility = View.VISIBLE
                    binding.sfBuyShimmer.visibility = View.GONE
                    if (buyList.isEmpty()) {
                        binding.llBuyEmptySearch.visibility = View.VISIBLE
                    }
                    buyAdapter.notifyDataSetChanged()
                } else {
                    binding.rvSellItems.visibility = View.VISIBLE
                    binding.sfSellShimmer.visibility = View.GONE
                    if (sellList.isEmpty()) {
                        binding.llSellEmptySearch.visibility = View.VISIBLE
                    }
                    sellAdapter.notifyDataSetChanged()
                }


            }
        } catch (e: Exception) {
            (requireActivity() as BaseActivity).showToast(getString(R.string.api_error))
            if (isBuy) {
                binding.rvBuyItems.visibility = View.VISIBLE
                binding.sfBuyShimmer.visibility = View.GONE
                if (buyList.isEmpty()) {
                    binding.llBuyEmptySearch.visibility = View.VISIBLE
                }
                buyAdapter.notifyDataSetChanged()
            } else {
                binding.rvSellItems.visibility = View.VISIBLE
                binding.sfSellShimmer.visibility = View.GONE
                if (sellList.isEmpty()) {
                    binding.llSellEmptySearch.visibility = View.VISIBLE
                }
                sellAdapter.notifyDataSetChanged()
            }
        }

    }


    private fun callApiFav(id: String, isFav: String) {
        var param = HashMap<String, String>()
        param.put("product_id", id)
        param.put("is_bookmark", isFav)

        if ((requireActivity() as BaseActivity).isInternetAvailable(requireActivity())) {

            SoniApp.mApiCall?.postAddRemoveProductFav(param)?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        (requireActivity() as BaseActivity).showToast(getString(R.string.api_error))
                    }

                    override fun onResponse(
                        call: Call<BaseModel>, response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                } else {

                                }
                            }
                        }
                    }
                })

        } else {
            (requireActivity() as BaseActivity).showToast(getString(R.string.internet_error))
        }
    }

    private fun moveToAddProduct() {
        if (SoniApp.user!!.firstName != null && SoniApp.user!!.firstName != "") {
            if (SoniApp.user!!.pincode != null && SoniApp.user!!.pincode != "") {
                var mIntent = Intent(requireContext(), AddEditProductsActivity::class.java)
                requireActivity().startActivity(mIntent)
            } else {
                SoniApp.showInquiryPopup(
                    requireActivity(),
                    arrayListOf(getString(R.string.your_pincode))
                )
            }
        } else {
            SoniApp.showInquiryPopup(
                requireActivity(),
                arrayListOf(getString(R.string.your_first_name), getString(R.string.your_pincode))
            )

        }

    }

    override fun onUpdate(location: Location) {
      if(buyList.isEmpty() && sellList.isEmpty()){
          if (binding.tabLayoutBuySell.selectedTabPosition == 0) {
              callApi(isBuy = true, isSearch = false, isPage = false)
          } else {
              callApi(isBuy = false, isSearch = false, isPage = false)

          }
      }
    }
}