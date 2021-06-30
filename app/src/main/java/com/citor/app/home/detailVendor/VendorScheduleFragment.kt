package com.citor.app.home.detailVendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citor.app.databinding.FragmentVendorScheduleBinding

class VendorScheduleFragment : Fragment() {

    private lateinit var vendorScheduleBinding: FragmentVendorScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vendorScheduleBinding =
            FragmentVendorScheduleBinding.inflate(layoutInflater, container, false)
        return vendorScheduleBinding.root
    }

}