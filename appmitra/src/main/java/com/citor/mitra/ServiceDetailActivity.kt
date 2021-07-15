package com.citor.mitra

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.citor.mitra.databinding.ActivityServiceDetailBinding

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var serviceDetailBinding: ActivityServiceDetailBinding

    companion object {
        const val name = "nama_pelanggan"
        const val timeService = "timeService"
        const val service = "service"
        const val price = "price"
        const val orderId = "orderId"
        const val paymentMethod = "paymentMethod"
        const val timestamp = "timestamp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceDetailBinding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(serviceDetailBinding.root)

        val name = intent.getStringExtra(name).toString()
        val timeService = intent.getStringExtra(timeService).toString()
        val service = intent.getStringExtra(service).toString()
        val price = intent.getStringExtra(price).toString()
        val orderId = intent.getStringExtra(orderId).toString()
        val paymentMethod = intent.getStringExtra(paymentMethod).toString()
        val timestamp = intent.getStringExtra(timestamp).toString()

        serviceDetailBinding.detailBookingName.text = name
        serviceDetailBinding.detailBookingTime.text = timeService
        serviceDetailBinding.detailBookingService.text = service
        serviceDetailBinding.detailBookingPrice.text = "Rp. $price"
        serviceDetailBinding.detailBookingOrder.text = orderId
        serviceDetailBinding.detailBookingPayment.text = paymentMethod
        serviceDetailBinding.detailBookingTimestamp.text = timestamp

        serviceDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ServiceDetailActivity, ServiceActivity::class.java))
        finish()
    }
}