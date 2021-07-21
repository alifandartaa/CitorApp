package com.citor.mitra

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.citor.mitra.adapter.AdapterRvService
import com.citor.mitra.databinding.ActivityServiceBinding
import com.citor.mitra.retrofit.DataService
import com.citor.mitra.retrofit.RetrofitClient
import com.citor.mitra.retrofit.response.ServiceResponse
import com.citor.mitra.utils.Constants
import com.citor.mitra.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceActivity : AppCompatActivity() {

    private lateinit var serviceBinding: ActivityServiceBinding
    private lateinit var adapterRvService: AdapterRvService
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceBinding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(serviceBinding.root)

        serviceBinding.btnBack.setOnClickListener{
            onBackPressed()
        }

        myPreferences = MySharedPreferences(this@ServiceActivity)

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val idmitra = myPreferences.getValue(Constants.VENDOR_ID).toString()
        adapterRvService = AdapterRvService()
        getPemesanan(idmitra, tokenAuth)
    }



    private fun getPemesanan(idmitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getPemesanan(idmitra, "Bearer $tokenAuth").enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(call: Call<ServiceResponse>, response: Response<ServiceResponse>) {
                if (response.isSuccessful) {
                    serviceBinding.spinKit.visibility = View.GONE
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data

                        adapterRvService.setListServiceItem(listData)
                        adapterRvService.notifyDataSetChanged()

                        with(serviceBinding.rvService) {
                            layoutManager = LinearLayoutManager(this@ServiceActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = adapterRvService
                        }
                    } else {
                        serviceBinding.svService.visibility = View.GONE
                        serviceBinding.rvListCatEmpty.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Toasty.error(this@ServiceActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}