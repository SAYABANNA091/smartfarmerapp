package com.soni.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.BuySelItemListAdapterShimmer
import com.soni.adapter.FavProductListAdapter
import com.soni.databinding.ActivityFavouriteProductsBinding
import com.soni.databinding.FragmentBuySellBinding
import com.soni.databinding.LayoutUserAccountListAdapterBinding
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.GetProductListResponse
import com.soni.services.web.models.InquiryListResponse
import com.soni.services.web.models.ProductData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteProductsActivity : BaseActivity() {
    lateinit var binding: ActivityFavouriteProductsBinding

    var pageNumber=1
    var totalPages=1
    var isLoading=false
    var favList:ArrayList<ProductData> = ArrayList()
    lateinit var favProductListAdapter: FavProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityFavouriteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        ivBack=findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favProductListAdapter = FavProductListAdapter(this,true,favList){pos: Int, isFav: Int ->

                callApiFav(id=favList[pos].id!!.toString(), isFav = isFav.toString())

        }
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.adapter = favProductListAdapter

        ivRight = findViewById(R.id.iv_right)
        ivBack =findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        title = findViewById(R.id.tv_title)
        title.text = getString(R.string.favourite)
        ivBack.setOnClickListener { this.onBackPressed() }

        binding.rvProductsShimmer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProductsShimmer.adapter = BuySelItemListAdapterShimmer (this,true)

        binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {

                    if (layoutManager.findLastCompletelyVisibleItemPosition() >= favProductListAdapter.itemCount - 10)
                    {
                        // call data
                        loadmore()
                        isLoading=true

                    }
                }
            }
        })
        callSearchApi(false)
        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            pageNumber = 1
            callSearchApi(false)
        })
    }


    private fun loadmore(){
        if(pageNumber< totalPages!!){
            pageNumber++
            callSearchApi(true)

        }
    }


    private fun callSearchApi(isPage:Boolean) {

        binding.tvNoFav.visibility=View.GONE
        if (!isPage) {
            binding.rvProducts.visibility = View.GONE
            binding.sfShimmer.visibility = View.VISIBLE
        }
        if (isInternetAvailable(this)) {

            var param: HashMap<String, String> = HashMap()
            param["Page"] = pageNumber.toString()
            param["Limit"] = "20"

            SoniApp.mApiCall?.postGetFavProductsList(param)
                ?.enqueue(object : Callback<GetProductListResponse> {
                    override fun onFailure(call: Call<GetProductListResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
//                        binding.rvRentItems.visibility = View.VISIBLE
//                        binding.sfShimmer.visibility = View.GONE

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
                                            favList.clear()
                                        }
                                        favList.addAll(responseLogin.data)
                                        favProductListAdapter.notifyDataSetChanged()
                                        binding.rvProducts.visibility = View.VISIBLE
                                        binding.sfShimmer.visibility = View.GONE

                                    }
                                    if(favList.isEmpty()){
                                        binding.tvNoFav.visibility=View.VISIBLE
                                    }
                                } else {
                                    binding.rvProducts.visibility = View.VISIBLE
                                    binding.sfShimmer.visibility = View.GONE
                                }

                            }
                        }
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvProducts.visibility = View.VISIBLE
            binding.sfShimmer.visibility = View.GONE

        }
    }


    private fun callApiFav(id: String,isFav:String) {
        var param = HashMap<String, String>()
        param.put("product_id", id)
        param.put("is_bookmark", isFav)

        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.postAddRemoveProductFav(param)
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
                                if (responseLogin.status!!) {
                                } else {

                                }
                            }
                        }
                    }
                })

        } else {
            showToast(getString(R.string.internet_error))
        }
    }
}