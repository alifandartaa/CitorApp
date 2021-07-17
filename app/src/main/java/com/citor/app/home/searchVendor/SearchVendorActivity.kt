package com.citor.app.home.searchVendor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.citor.app.R
import com.citor.app.databinding.ActivitySearchVendorBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.google.android.gms.location.*
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchVendorActivity : AppCompatActivity() {

    private lateinit var searchVendorBinding: ActivitySearchVendorBinding
    private lateinit var searchVendorAdapter: SearchVendorAdapter
    private lateinit var myPreferences: MySharedPreferences
    private var listVendorSearch: ArrayList<VendorItemEntity> = arrayListOf()
    private var listVendorStatusOpen: ArrayList<VendorItemEntity> = arrayListOf()
    private var listVendorStatusClose: ArrayList<VendorItemEntity> = arrayListOf()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val PERMISSION_ID = 1010
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchVendorBinding = ActivitySearchVendorBinding.inflate(layoutInflater)
        setContentView(searchVendorBinding.root)

        myPreferences = MySharedPreferences(this@SearchVendorActivity)

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        searchVendorBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermission()

        getLastLocation()

        searchVendorBinding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchVendorAdapter.filter.filter(query)
                return true
            }
        })

        searchVendorAdapter = SearchVendorAdapter()
        setupListItemVendor(tokenAuth)
        showLoading(true)

        val areaCategory = resources.getStringArray(R.array.area_category)
        val arrayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, areaCategory)
        searchVendorBinding.ctvArea.setAdapter(arrayAdapter)

        searchVendorBinding.ctvArea.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    listVendorStatusClose.clear()
                    listVendorSearch.forEach { vendorItem ->
                        if (vendorItem.statusBuka == "buka") {
                            listVendorStatusOpen.add(vendorItem)
                        }
                    }
                    searchVendorAdapter.setListVendorItem(listVendorStatusOpen)
                }
                1 -> {
                    listVendorStatusOpen.clear()
                    listVendorSearch.forEach { vendorItem ->
                        if (vendorItem.statusBuka == "tutup") {
                            listVendorStatusClose.add(vendorItem)
                        }
                    }
                    searchVendorAdapter.setListVendorItem(listVendorStatusClose)
                }
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            val geoCoder = Geocoder(this@SearchVendorActivity, Locale.getDefault())
            val address = geoCoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1)
            val roadName = address[0].thoroughfare
            val number = address[0].featureName
            val cityName = address[0].subAdminArea
            val currentUserLocation = "$roadName $number $cityName"
            searchVendorBinding.tvLocation.text = currentUserLocation
        }
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location: Location? = it.result
                    if (location == null) {
                        val locationRequest = LocationRequest()
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationRequest.interval = 10000
                        locationRequest.fastestInterval = 5000
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude + location.latitude)
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        val address = geoCoder.getFromLocation(location.latitude, location.longitude, 3)
                        val roadName = address[0].thoroughfare
                        val number = address[0].featureName
                        val cityName = address[0].subAdminArea
                        val currentUserLocation = "$roadName $number $cityName"
                        searchVendorBinding.tvLocation.text = currentUserLocation
                    }
                }
            } else {
                Toast.makeText(this, "Mohon aktifkan GPS Location Anda", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
                getLastLocation()
            }
        }
    }

    private fun setupListItemVendor(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getMitra("Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data
                        listVendorSearch = listData
                        searchVendorAdapter.setListVendorItem(listVendorSearch)
                        showLoading(false)
                        searchVendorAdapter.notifyDataSetChanged()

                        with(searchVendorBinding.rvLocation) {
                            layoutManager = LinearLayoutManager(this@SearchVendorActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = searchVendorAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(this@SearchVendorActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            searchVendorBinding.progressBar.visibility = View.VISIBLE
        } else {
            searchVendorBinding.progressBar.visibility = View.GONE
        }
    }

    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
