package com.example.citorapp.home.searchVendor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citorapp.databinding.ActivitySearchVendorBinding

class SearchVendorActivity : AppCompatActivity() {

    private lateinit var searchVendorBinding: ActivitySearchVendorBinding
    private lateinit var searchVendorAdapter: SearchVendorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchVendorBinding = ActivitySearchVendorBinding.inflate(layoutInflater)
        setContentView(searchVendorBinding.root)

        searchVendorBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        searchVendorAdapter = SearchVendorAdapter()
        setupListItemVendor()
    }

    private fun setupListItemVendor() {
        showLoading(true)
        val listDummy = ArrayList<VendorItemEntity>()
        val data1 = VendorItemEntity("Jaya Makmur Wash", "Jl. MT Haryono IX No.13", true)
        val data2 = VendorItemEntity("Putra Cuci Motor", "Jl. MT Haryono II No. 9", true)
        val data3 = VendorItemEntity("Klaker Wash", "JL. Klayatan 3 No.16C", false)
        listDummy.add(data1)
        listDummy.add(data2)
        listDummy.add(data3)
        searchVendorAdapter.setListVendorItem(listDummy)
        showLoading(false)
        searchVendorAdapter.notifyDataSetChanged()

        with(searchVendorBinding.rvLocation) {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = searchVendorAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            searchVendorBinding.progressBar.visibility = View.VISIBLE
        } else {
            searchVendorBinding.progressBar.visibility = View.GONE
        }
    }
}
