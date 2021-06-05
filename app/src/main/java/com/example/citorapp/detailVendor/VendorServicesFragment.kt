package com.example.citorapp.detailVendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citorapp.databinding.FragmentVendorServicesBinding


class VendorServicesFragment : Fragment() {

    private lateinit var vendorServicesBinding: FragmentVendorServicesBinding
    private lateinit var vendorWashTypeAdapter: VendorWashTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vendorServicesBinding =
            FragmentVendorServicesBinding.inflate(layoutInflater, container, false)
        return vendorServicesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            vendorWashTypeAdapter = VendorWashTypeAdapter()
            setupListWashType()
        }
    }

    private fun setupListWashType() {
        showLoading(true)

        val listDummy = ArrayList<WashTypeEntity>()
        val data1 = WashTypeEntity("tes", "Layanan Cuci Dasar")
        val data2 = WashTypeEntity("tes", "Layanan Cuci Dasar dan Cuci Lebih Dalam")
        val data3 = WashTypeEntity("tes", "Layanan Cuci Dasar dan Cuci Busa")
        listDummy.add(data1)
        listDummy.add(data2)
        listDummy.add(data3)
        vendorWashTypeAdapter.setListWashType(listDummy)
        showLoading(false)
        vendorWashTypeAdapter.notifyDataSetChanged()

        with(vendorServicesBinding.rvWashType) {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = vendorWashTypeAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            vendorServicesBinding.progressBar.visibility = View.VISIBLE
        } else {
            vendorServicesBinding.progressBar.visibility = View.GONE
        }
    }
}