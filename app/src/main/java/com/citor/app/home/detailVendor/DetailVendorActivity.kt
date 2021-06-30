package com.citor.app.home.detailVendor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityDetailVendorBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailVendorActivity : AppCompatActivity() {

    private lateinit var activityDetailVendorBinding: ActivityDetailVendorBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_vendor_text_1,
            R.string.tab_vendor_text_2
        )

        const val vendorId = "vendor_id"
        const val vendorName = "vendor_name"
        const val vendorAddress = "vendor_address"
        const val lat = "latitude_vendor"
        const val long = "longitude_vendor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailVendorBinding = ActivityDetailVendorBinding.inflate(layoutInflater)
        setContentView(activityDetailVendorBinding.root)

        val idVendor= intent.getStringExtra(vendorId).toString()
        val vendorPagerAdapter = DetailVendorPagerAdapter(this)
        vendorPagerAdapter.getVendorId(idVendor)
        activityDetailVendorBinding.vpDetailWasher.adapter = vendorPagerAdapter
        TabLayoutMediator(
            activityDetailVendorBinding.tabsDetailWasher,
            activityDetailVendorBinding.vpDetailWasher
        ) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        activityDetailVendorBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        activityDetailVendorBinding.tvVendorName.text = intent.getStringExtra(vendorName)
        activityDetailVendorBinding.tvVendorAddress.text = intent.getStringExtra(vendorAddress)

        activityDetailVendorBinding.tvVendorMap.setOnClickListener {
            val name = intent.getStringExtra(vendorName).toString()
            val latitude = intent.getStringExtra(lat).toString()
            val longitude = intent.getStringExtra(long).toString()
            val uri = "http://maps.google.com/maps?q=loc:$latitude,$longitude ($name)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }
}