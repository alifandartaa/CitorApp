package com.citor.app.home.detailBooking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.citor.app.R
import com.citor.app.databinding.ActivityBookingBinding
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.example.citorapp.retrofit.DataService
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingActivity : AppCompatActivity() {

    private lateinit var bookingBinding: ActivityBookingBinding
    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val vendorId = "vendor_id"
        const val service = "service"
        const val price = "price"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingBinding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(bookingBinding.root)
        myPreferences = MySharedPreferences(this@BookingActivity)

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        bookingBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        val idVendor = intent.getStringExtra(vendorId).toString()
        val service = intent.getStringExtra(service).toString()
        val price = intent.getStringExtra(price).toString()
        val listInfo = ArrayList<String>()
        listInfo.add(idVendor)
        listInfo.add(service)
        listInfo.add(price)

        bookingAdapter = BookingAdapter(listInfo)
        setupListItemBooking(idVendor, tokenAuth)
    }

    private fun setupListItemBooking(idMitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getJamBuka(idMitra, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse>{
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data
                        bookingAdapter.setListBookingItem(listData)
                        bookingAdapter.notifyDataSetChanged()
                        with(bookingBinding.rvBooking) {
                            layoutManager = GridLayoutManager(context, 2)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = bookingAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(this@BookingActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
//        showLoading(true)
//        showLoading(false)

    }
}