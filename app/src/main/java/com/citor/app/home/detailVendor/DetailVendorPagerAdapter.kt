package com.citor.app.home.detailVendor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailVendorPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    val bundle = Bundle()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = makeVendorService()
            1 -> fragment = makeVendorSchedule()
        }
        return fragment as Fragment
    }

    private fun makeVendorService(): Fragment? {
        val fragment1: Fragment?
        fragment1 = VendorServicesFragment()
        fragment1.arguments = bundle
        return fragment1
    }

    private fun makeVendorSchedule(): Fragment? {
        val fragment1: Fragment?
        fragment1 = VendorScheduleFragment()
        fragment1.arguments = bundle
        return fragment1
    }

    fun getVendorIdAndName(vendorId: String, vendorName: String) {
        bundle.putString("id", vendorId)
        bundle.putString("vendor_name", vendorName)
    }

}