package com.soni.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxTextView
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.SoniApp.Companion.changeAppLanguage
import com.soni.SoniApp.Companion.fusedLocationClient
import com.soni.adapter.*
import com.soni.databinding.ActivityWeatherBinding
import com.soni.services.web.models.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit


class WeatherActivity : BaseActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private var hourlyForecast: ArrayList<HourlyForecast> = ArrayList()
    private var DailyForecasts: ArrayList<DailyForecasts> = ArrayList()

    private var locationKey:String =""
    var locationsList: ArrayList<LocationSearchResult> = ArrayList()
    lateinit var autoAdapter: AutoCompleteAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS_LOC,
            SoniApp.REQUEST_CODE_PERMISSIONS
        )
        init()

    }

    @SuppressLint("CheckResult")
    private fun init() {
        ivBack = findViewById(R.id.iv_back)
        title = findViewById(R.id.tv_title)
        ivRight = findViewById(R.id.iv_right)

        ivBack.setOnClickListener { finish() }
        ivRight.visibility = View.INVISIBLE
        title.text = getString(R.string.weather)

        binding.rvDayForcast.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDayForcast.adapter = WeatherDateAdapter(this, hourlyForecast){
            setForecastData(it)

        }

        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
        }


        binding.rvHourForecastShimmer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourForecastShimmer.adapter = WeatherDateAdapterShimmer(this)

        binding.rvWeekForecast.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvWeekForecast.adapter = WeeksForecastAdapter(this, DailyForecasts)

        binding.rvWeekForecastShimmer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvWeekForecastShimmer.adapter = InquiriesHomeAdapterShimmer(this)


        locationCalllistener={
            callGetLocationData("${it!!.latitude},${it!!.longitude!!}")
        }
        getLocation()

        binding.sfHourForecastShimmer.visibility = View.VISIBLE
        binding.sfCurrentWeatherShimmer.visibility = View.VISIBLE
        binding.sfWeekForecastShimmer.visibility = View.VISIBLE
        binding.llCurrentWeather.visibility = View.GONE
        binding.llCurrentWeatherStats.visibility = View.GONE

         autoAdapter =
             AutoCompleteAdapter(this, locationsList){
                 locationKey=locationsList[it].Key!!
                 callHourlyForecasts()
                 callWeekForecasts()
                 binding.tvCurrentLoc.text=locationsList[it].LocalizedName!!+", "+locationsList[it].AdministrativeArea!!.LocalizedName!!
                 binding.etSearch.clearFocus()
                 hideKeyboard(binding.root)
             }
        binding.spLoc.adapter = autoAdapter
        val popup: Field =
            AppCompatSpinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow = popup[binding.spLoc] as
                androidx.appcompat.widget.ListPopupWindow
        popupWindow.height = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp).toInt()

        RxTextView.textChanges(binding.etSearch)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { charSequence ->
                charSequence.toString()
            }
            .subscribe { string ->
                if (string.length > 3) {
                    retrieveData(string!!)
                } else {
                    binding.spLoc.clearFocus()

                }
            }

    }

    private fun retrieveData(s: String) {
        val text = s.toString()
        if (text.contains(" ")) {
            text.replace(" ", "%20")
        }

        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?. getLocationList(s)
                ?.enqueue(object : Callback<ArrayList<LocationSearchResult>> {
                    override fun onFailure(call: Call<ArrayList<LocationSearchResult>>, t: Throwable) {
                        showToast(getString(R.string.api_error))

                    }

                    override fun onResponse(
                        call: Call<ArrayList<LocationSearchResult>>,
                        response: Response<ArrayList<LocationSearchResult>>?
                    ) {
                        try {
                            if (response != null) {
                                val responseLogin = response.body()

                                locationsList.clear()
                                locationsList.addAll(responseLogin!!)
                                if(locationsList.isEmpty()){
                                    locationsList.add(LocationSearchResult(Key = locationKey, LocalizedName = getString(
                                                                            R.string.no_data_found)))
                                }
//                                autoAdapter.notifyDataSetChanged()
                                binding.spLoc.performClick()
                            }
                        } catch (e: Exception) {
                            showToast(getString(R.string.no_data_found))

                        }

                    }
                })
        } else {
            showToast(getString(R.string.internet_error))

        }
    }

    fun callHourlyForecasts() {
        binding.rvDayForcast.visibility = View.GONE
        binding.llCurrentWeatherStats.visibility = View.GONE
        binding.llCurrentWeather.visibility = View.GONE

        binding.sfHourForecastShimmer.visibility = View.VISIBLE
        binding.sfCurrentWeatherShimmer.visibility = View.VISIBLE
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.getHourlyForecast(locationKey)
                ?.enqueue(object : Callback<ArrayList<HourlyForecast>> {
                    override fun onFailure(call: Call<ArrayList<HourlyForecast>>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.rvDayForcast.visibility = View.VISIBLE
                        binding.llCurrentWeather.visibility = View.VISIBLE
                        binding.llCurrentWeatherStats.visibility = View.VISIBLE
                        binding.sfHourForecastShimmer.visibility = View.GONE
                        binding.sfCurrentWeatherShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<ArrayList<HourlyForecast>>,
                        response: Response<ArrayList<HourlyForecast>>?
                    ) {
                        try {
                            if (response != null) {
                                val responseLogin = response.body()
                                hourlyForecast.clear()
                                hourlyForecast.addAll(responseLogin!!)
                                binding.rvDayForcast.adapter!!.notifyDataSetChanged()
                                setForecastData(0)

                            }
                            binding.rvDayForcast.visibility = View.VISIBLE
                            binding.sfHourForecastShimmer.visibility = View.GONE
                            binding.llCurrentWeather.visibility = View.VISIBLE
                            binding.llCurrentWeatherStats.visibility = View.VISIBLE
                            binding.sfCurrentWeatherShimmer.visibility = View.GONE
                        } catch (e: Exception) {
                            showToast(getString(R.string.api_error))
                        }

                    }
                })
        } else {
            showToast(getString(R.string.internet_error))
            binding.rvDayForcast.visibility = View.VISIBLE
            binding.sfHourForecastShimmer.visibility = View.GONE
            binding.llCurrentWeather.visibility = View.VISIBLE
            binding.llCurrentWeatherStats.visibility = View.VISIBLE
            binding.sfCurrentWeatherShimmer.visibility = View.GONE

        }
    }
    fun setForecastData(pos : Int){
        binding.tvCurrentTemp.text =
            hourlyForecast[pos].Temperature!!.Value!!.toString()
        binding.tvCurrentTempIcon.setImageResource(
            (SoniApp.getWeatherIcon(
                hourlyForecast[pos].WeatherIcon!!.toInt()
            ))
        )
        binding.tvFeelTemp.text =
            hourlyForecast[pos].RealFeelTemperature!!.Value!!.toString()
        binding.tvHymidity.text = hourlyForecast[pos].RelativeHumidity!! + "%"
        binding.tvWindSpeed.text =
            hourlyForecast[pos].Wind!!.Speed!!.Value.toString() + " " + hourlyForecast[0].Wind!!.Speed!!.Unit.toString()
        binding.tvRain.text =
            hourlyForecast[pos].RainProbability.toString() + "%"
    }

    fun callWeekForecasts() {
        binding.rvWeekForecast.visibility = View.GONE
        binding.sfWeekForecastShimmer.visibility = View.VISIBLE
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.getWeekForecast(locationKey)?.enqueue(object : Callback<WeekForecast> {
                    override fun onFailure(call: Call<WeekForecast>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.rvWeekForecast.visibility = View.VISIBLE
                        binding.sfWeekForecastShimmer.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<WeekForecast>, response: Response<WeekForecast>?
                    ) {
                        try {
                            if (response != null) {
                                val responseLogin = response.body()
                                DailyForecasts.clear()
                                DailyForecasts.addAll(responseLogin!!.DailyForecasts)
                                binding.rvWeekForecast.adapter!!.notifyDataSetChanged()
                            }
                            binding.rvWeekForecast.visibility = View.VISIBLE
                            binding.sfWeekForecastShimmer.visibility = View.GONE
                        } catch (e: Exception) {
                            showToast(getString(R.string.api_error))
                        }

                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvWeekForecast.visibility = View.VISIBLE
            binding.sfWeekForecastShimmer.visibility = View.GONE

        }
    }

 fun callGetLocationData(loc:String) {
        if (isInternetAvailable(this)) {

            SoniApp.mApiCall?.getLocationCode(loc)?.enqueue(object : Callback<LocationIdResponse> {
                    override fun onFailure(call: Call<LocationIdResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))


                    }

                    override fun onResponse(
                        call: Call<LocationIdResponse>, response: Response<LocationIdResponse>?
                    ) {
                        try {
                            if (response != null) {
                                val responseLogin = response.body()
                                locationKey=responseLogin!!.Key!!
                                binding.tvCurrentLoc.text="${responseLogin.EnglishName},${responseLogin.Country!!.EnglishName}"
                                callHourlyForecasts()
                                callWeekForecasts()

                            }
                        } catch (e: Exception) {
                            showToast(getString(R.string.api_error))
                        }

                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binding.rvWeekForecast.visibility = View.VISIBLE
            binding.sfWeekForecastShimmer.visibility = View.GONE

        }
    }

}