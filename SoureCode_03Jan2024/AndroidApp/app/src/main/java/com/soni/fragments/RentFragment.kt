package com.soni.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.BaseActivity
import com.soni.activity.HomeActivity
import com.soni.activity.RentListActivity
import com.soni.adapter.RentItemListAdapter
import com.soni.adapter.RentItemListAdapterShimmer
import com.soni.adapter.RentTypeAdapter
import com.soni.databinding.FragmentRentBinding
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.PropertySearchResponse
import com.soni.utils.Const
import com.soni.utils.LocationCallbackListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentFragment : BaseFragment(), LocationCallbackListener {

    lateinit var rentTypeAdapter: RentTypeAdapter
    lateinit var rentTypeLayoutManager: LinearLayoutManager

    lateinit var rentItemsAdapter: RentItemListAdapter
    lateinit var rentItemsLayoutManager: LinearLayoutManager

    lateinit var binding: FragmentRentBinding
    var propertyList: ArrayList<PropertyModel> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRentBinding.inflate(layoutInflater)
        HomeActivity.locationCall.add(this@RentFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        binding.tvHello.text =
            "${getString(R.string.hello_user)} ${if (SoniApp.user!!.firstName != null) " " + SoniApp.user!!.firstName else " "}!"
        if (SoniApp.location != null) {

        } else {
            HomeActivity.activity!!.getLocation()
        }
        super.onResume()
    }

    private fun init() {


        rentTypeAdapter = RentTypeAdapter(requireActivity()) { it ->
            if (binding.etSearch.text.toString().isEmpty()) {
                binding.rvTypes.scrollToPosition(it)
                binding.etSearch.setText("")
                callSearchApi("")
            } else {
                rentTypeAdapter.seletedPos = -1
                rentTypeAdapter.notifyDataSetChanged()
            }
        }
        rentTypeLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
            rentTypeAdapter.seletedPos = 0
            binding.rvTypes.scrollToPosition(0)
            rentTypeAdapter.notifyDataSetChanged()
            callSearchApi("")
        }

        binding.fab.setOnClickListener {
            if (SoniApp.user!!.firstName != null && SoniApp.user!!.firstName != "") {
                if (SoniApp.user!!.pincode != null && SoniApp.user!!.pincode != "") {
                    val bundle = Bundle()
                    bundle.putInt("screenType", 2)
                    (requireActivity() as HomeActivity).seEditPropertiesFromRentFragment(
                        bundle,
                        Const.TAB.rent
                    )
                } else {
                    SoniApp.showInquiryPopup(
                        requireActivity(),
                        arrayListOf(getString(R.string.your_pincode))
                    )
                }
            } else {
                SoniApp.showInquiryPopup(
                    requireActivity(),
                    arrayListOf(
                        getString(R.string.your_first_name),
                        getString(R.string.your_pincode)
                    )
                )

            }


        }

        binding.rvTypes.adapter = rentTypeAdapter
        binding.rvTypes.layoutManager = rentTypeLayoutManager

        rentItemsAdapter = RentItemListAdapter(requireActivity(), propertyList)
        rentItemsLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvRentItems.adapter = rentItemsAdapter
        binding.rvRentItems.layoutManager = rentItemsLayoutManager



        binding.rvRentItemsShimmer.adapter = RentItemListAdapterShimmer(requireActivity())
        binding.rvRentItemsShimmer.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->

            (requireActivity() as BaseActivity).hideKeyboard(binding.etSearch)
            rentTypeAdapter.seletedPos = -1
            rentTypeAdapter.notifyDataSetChanged()

            callSearchApi(binding.etSearch.text.toString())
            true
        }

        binding.etSearch.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llSearch, b)
        }
//        callSearchApi("")
        binding.llEmptySearch.visibility = View.VISIBLE
        binding.tvSeeAll.setOnClickListener {
            var mIntent = Intent(requireActivity() as HomeActivity, RentListActivity::class.java)
            if (binding.etSearch.text.toString().isNotEmpty()) {
                mIntent.putExtra(Const.IntentKey.PropertySearch, binding.etSearch.text.toString())
            } else {
                mIntent.putExtra(
                    Const.IntentKey.PropertyType,
                    (rentTypeAdapter.seletedPos + 1).toString()
                )

            }

            requireActivity().startActivity(mIntent)
        }
        if (SoniApp.location != null) {
            callSearchApi("")
        } else {
            HomeActivity.activity!!.getLocation()
        }
    }

    private fun callSearchApi(search: String) {
        try {
            if (SoniApp.location != null) {

            } else {
                HomeActivity.activity!!.getLocation()
                return
            }
            binding.rvRentItems.visibility = View.GONE
            binding.sfShimmer.visibility = View.VISIBLE
            binding.llEmptySearch.visibility = View.GONE
            if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

                var param: HashMap<String, String> = HashMap()
                if (search.isNotEmpty()) {
                    param["text"] = search
                } else {
                    param["property_type_id"] = (rentTypeAdapter.seletedPos + 1).toString()
                }

                param["Page"] = "1"
                param["Limit"] = "10"
                param["Latitude"] = SoniApp.location!!.latitude.toString()
                param["Longitude"] = SoniApp.location!!.latitude.toString()

                if (SoniApp.user != null) {
                    if (SoniApp.user!!.pincode != null && SoniApp.user!!.pincode.toString()
                            .isNotEmpty()
                    ) {
                        param["pincode"] = SoniApp.user!!.pincode!!
                    } else {
                        param["pincode"] = ""
                    }
                } else {
                    param["pincode"] = ""
                }
                SoniApp.mApiCall?.postSearchProperty(param)
                    ?.enqueue(object : Callback<PropertySearchResponse> {
                        override fun onFailure(call: Call<PropertySearchResponse>, t: Throwable) {
                            (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                            binding.rvRentItems.visibility = View.VISIBLE
                            binding.sfShimmer.visibility = View.GONE

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
                                            propertyList.clear()
                                            propertyList.addAll(responseLogin.data)
                                            rentItemsAdapter.notifyDataSetChanged()
                                            if (propertyList.isEmpty()) {
                                                binding.sfShimmer.visibility = View.GONE
                                                binding.llEmptySearch.visibility = View.VISIBLE
                                            } else {
                                                binding.rvRentItems.visibility = View.VISIBLE
                                                binding.sfShimmer.visibility = View.GONE
                                            }

                                        }
                                    } else {
                                        binding.sfShimmer.visibility = View.GONE
                                        binding.llEmptySearch.visibility = View.VISIBLE
                                    }

                                }
                            }
                        }


                    })

            } else {
                (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
                binding.rvRentItems.visibility = View.VISIBLE
                binding.sfShimmer.visibility = View.GONE

            }
        } catch (e: Exception) {
            // handler
        } finally {
            // optional finally block
        }


    }

    override fun onUpdate(location: Location) {
        if (propertyList.isEmpty()) {
            callSearchApi("")
        }
    }
}

//postSearchProperty