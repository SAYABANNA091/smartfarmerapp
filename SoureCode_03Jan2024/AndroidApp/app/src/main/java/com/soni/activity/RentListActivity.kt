package com.soni.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.RentItemListAdapter
import com.soni.adapter.RentItemListAdapterShimmer
import com.soni.databinding.ActivityRentListBinding
import com.soni.databinding.FragmentRentBinding
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.PropertySearchResponse
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentListActivity : BaseActivity() {
  lateinit var binding : ActivityRentListBinding
    lateinit var rentItemsAdapter: RentItemListAdapter
    lateinit var rentItemsLayoutManager: LinearLayoutManager
    var typeId:String =""
    var propertySearch:String =""
    var pageNumber=1
    var totalPages=1
    var isLoading=false

    var propertyList: ArrayList<PropertyModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
//        setContentView(R.layout.activity_rent_list)
        binding = ActivityRentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        typeId=intent.extras!!.getString(Const.IntentKey.PropertyType,"")
        propertySearch=intent.extras!!.getString(Const.IntentKey.PropertySearch,"")

        init()
    }

  fun  init() {
      ivBack = findViewById(R.id.iv_back)
      ivRight = findViewById(R.id.iv_right)
      title = findViewById(R.id.tv_title)
      title.text = getString(R.string.rent_title)
      ivRight.visibility = View.INVISIBLE
      ivBack.setOnClickListener { finish() }
        rentItemsAdapter = RentItemListAdapter(this, propertyList)
        rentItemsLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvRentItems.adapter = rentItemsAdapter
        binding.rvRentItems.layoutManager = rentItemsLayoutManager



        binding.rvRentItemsShimmer.adapter = RentItemListAdapterShimmer(this)
        binding.rvRentItemsShimmer.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
      binding.rvRentItems.addOnScrollListener(object : RecyclerView.OnScrollListener()
      {
          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
          {
              super.onScrolled(recyclerView, dx, dy)
              if (!isLoading)
              {

                  if (rentItemsLayoutManager.findLastCompletelyVisibleItemPosition() >= rentItemsAdapter.itemCount - 5)
                  {
                      // call data
                      loadmore()
                      isLoading=true

                  }
              }
          }
      })
      if (SoniApp.location!=null) {
          callSearchApi(false)
      }else{
          getLocation()
      }

      locationCalllistener={

          callSearchApi(false)
      }
  }

  private fun loadmore(){
        if(pageNumber< totalPages!!){
            pageNumber++
            callSearchApi(true)

        }
    }


    private fun callSearchApi(isPage:Boolean) {
        if (!isPage) {
            binding.rvRentItems.visibility = View.GONE
            binding.sfShimmer.visibility = View.VISIBLE
        }
        if (isInternetAvailable(this)) {

            var param: HashMap<String, String> = HashMap()
            if(propertySearch.isNotEmpty()) {
                param["text"] = propertySearch
            }
            else{

                param["property_type_id"] = typeId

            }
            param["Page"] = pageNumber.toString()
            param["Limit"] = "10"
            param["Latitude"] = SoniApp.location!!.latitude.toString()
            param["Longitude"] = SoniApp.location!!.longitude.toString()

            SoniApp.mApiCall?.postSearchProperty(param)
                ?.enqueue(object : Callback<PropertySearchResponse> {
                    override fun onFailure(call: Call<PropertySearchResponse>, t: Throwable) {
                       showToast(getString(R.string.api_error))
                        binding.rvRentItems.visibility = View.VISIBLE
                        binding.sfShimmer.visibility = View.GONE
                        isLoading=false
                    }

                    override fun onResponse(
                        call: Call<PropertySearchResponse>,
                        response: Response<PropertySearchResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        if (!isPage) {
                                            propertyList.clear()
                                        }
                                        propertyList.addAll(responseLogin.data)
                                        rentItemsAdapter.notifyDataSetChanged()
                                        if (propertyList.isEmpty()) {
                                            binding.sfShimmer.visibility = View.GONE
                                        } else {
                                            binding.rvRentItems.visibility = View.VISIBLE
                                            binding.sfShimmer.visibility = View.GONE
                                        }
                                        totalPages=responseLogin.totalpage!!
                                    }
                                } else {
                                    binding.sfShimmer.visibility = View.GONE
                                }

                            }
                            isLoading=false
                        }
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvRentItems.visibility = View.VISIBLE
            binding.sfShimmer.visibility = View.GONE
            isLoading=false
        }
    }
}