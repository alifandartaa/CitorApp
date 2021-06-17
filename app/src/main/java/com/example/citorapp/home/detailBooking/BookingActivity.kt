package com.example.citorapp.home.detailBooking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.citorapp.databinding.ActivityBookingBinding

class BookingActivity : AppCompatActivity() {

    private lateinit var bookingBinding: ActivityBookingBinding
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookingBinding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(bookingBinding.root)

        bookingBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        bookingAdapter = BookingAdapter()
        setupListItemBooking()
    }

    private fun setupListItemBooking() {
//        showLoading(true)
//        val listDummy = ArrayList<VendorItemEntity>()
//        val data1 = VendorItemEntity("Jaya Makmur Wash", "Jl. MT Haryono IX No.13", true)
//        val data2 = VendorItemEntity("Putra Cuci Motor", "Jl. MT Haryono II No. 9", true)
//        val data3 = VendorItemEntity("Klaker Wash",  "JL. Klayatan 3 No.16C", false)
//        listDummy.add(data1)
//        listDummy.add(data2)
//        listDummy.add(data3)
        bookingAdapter.setListBookingItem(BookingData.listData)
//        showLoading(false)
        bookingAdapter.notifyDataSetChanged()

        with(bookingBinding.rvBooking) {
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = bookingAdapter
        }
    }
}