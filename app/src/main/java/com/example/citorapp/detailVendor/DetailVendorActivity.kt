package com.example.citorapp.detailVendor

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.ProfileTestActivity
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityDetailVendorBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailVendorActivity : AppCompatActivity() {

    private lateinit var activityDetailVendorBinding: ActivityDetailVendorBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_vendor_text_1,
            R.string.tab_vendor_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailVendorBinding = ActivityDetailVendorBinding.inflate(layoutInflater)
        setContentView(activityDetailVendorBinding.root)

        val vendorPagerAdapter = DetailVendorPagerAdapter(this)
        activityDetailVendorBinding.vpDetailWasher.adapter = vendorPagerAdapter
        TabLayoutMediator(
            activityDetailVendorBinding.tabsDetailWasher,
            activityDetailVendorBinding.vpDetailWasher
        ) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        activityDetailVendorBinding.tvVendorMap.setOnClickListener {
            val intent = Intent(this, ProfileTestActivity::class.java)
            startActivity(intent)
        }
    }
}