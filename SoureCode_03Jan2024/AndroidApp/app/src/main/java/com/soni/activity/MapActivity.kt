package com.soni.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityMapBinding
import com.soni.databinding.ActivityWeatherBinding
import com.soni.databinding.FragmentBuySellBinding

class MapActivity : BaseActivity() , OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    private lateinit var mGoogleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapActivity)

        binding.btnPickLocation.setOnClickListener {
            SoniApp.mapCallbacks.forEach {
                it.onUpdate( mGoogleMap!!.cameraPosition.target.latitude,
                    mGoogleMap!!.cameraPosition.target.longitude)
            }
            showToast(getString(R.string.picked_location_is_set))
        finish()
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        // Set a default location, for example, your city
        val defaultLocation = LatLng(
            SoniApp.location!!.latitude,
            SoniApp.location!!.longitude
        )
        Log.d("Map",defaultLocation.toString())
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 17f))


    }
}