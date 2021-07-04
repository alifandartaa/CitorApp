package com.citor.app.home.searchVendor

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.citor.app.R
import com.citor.app.databinding.ActivitySearchVendorBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
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
    private var listVendorResultSearch: ArrayList<VendorItemEntity> = arrayListOf()
//    lateinit var client: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_CODE = 101
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

//        client = LocationServices.getFusedLocationProviderClient(this)
//        val geoCoder = Geocoder(this, Locale.getDefault())
//        var addresses:List<Address>
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        client.lastLocation.addOnCompleteListener {
//            val latitude = it.result.latitude
//            val longitude = it.result.longitude
//            addresses = geoCoder.getFromLocation(latitude, longitude, 1)
//
//            val address: String = addresses[0].getAddressLine(0)
//            Log.d("Alamat", address)
//            searchVendorBinding.tvLocation.text = address
//        }

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

        searchVendorBinding.ctvArea.setOnItemClickListener { parent, view, position, id ->
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

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == LOCATION_PERMISSION_CODE && grantResults.size > 0){
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getCurrentAddress()
//            }else{
//                Toast.makeText(this, "Permission Location Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

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

}
