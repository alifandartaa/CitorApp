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
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.example.citorapp.retrofit.DataService
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchVendorActivity : AppCompatActivity() {

    private lateinit var searchVendorBinding: ActivitySearchVendorBinding
    private lateinit var searchVendorAdapter: SearchVendorAdapter
    private lateinit var myPreferences: MySharedPreferences
//    private lateinit var itemlist: ArrayList<VendorItemEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchVendorBinding = ActivitySearchVendorBinding.inflate(layoutInflater)
        setContentView(searchVendorBinding.root)


        myPreferences = MySharedPreferences(this@SearchVendorActivity)

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        searchVendorBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }


        searchVendorBinding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchVendorAdapter.filter.filter(query)
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
    }

    private fun setupListItemVendor(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getMitra("Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data
                        searchVendorAdapter.setListVendorItem(listData)
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
