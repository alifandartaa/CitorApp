package com.example.citorapp.home.detailBooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citorapp.R

class BookingActivity : AppCompatActivity() {

    private lateinit var rvBooking: RecyclerView
    private var list: ArrayList<BookingModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        rvBooking = findViewById(R.id.rv_booking)
        list.addAll(BookingData.listData)
        showRecyclerGrid()
    }

    private fun showRecyclerGrid() {
        rvBooking.layoutManager = GridLayoutManager(this, 2)
        val gridBookingAdapter = BookingAdapter(list)
        rvBooking.adapter = gridBookingAdapter
    }
}