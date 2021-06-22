package com.example.citorapp.home.detailVendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citorapp.R
import com.example.citorapp.databinding.FragmentVendorServicesBinding
import com.example.citorapp.retrofit.DataService
import com.example.citorapp.retrofit.RetrofitClient
import com.example.citorapp.retrofit.response.MitraResponse
import com.example.citorapp.utils.Constants
import com.example.citorapp.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorServicesFragment : Fragment() {

    private lateinit var vendorServicesBinding: FragmentVendorServicesBinding
    private lateinit var vendorWashTypeAdapter: VendorWashTypeAdapter
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vendorServicesBinding = FragmentVendorServicesBinding.inflate(layoutInflater, container, false)
        return vendorServicesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPreferences = MySharedPreferences(requireContext())

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        if (activity != null) {
            val id = arguments?.getString("id").toString()
            vendorWashTypeAdapter = VendorWashTypeAdapter(id)
            setupListWashType(id, tokenAuth)
            showLoading(true)
        }
    }

    private fun setupListWashType(idmitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getLayanan(idmitra, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data

                        vendorWashTypeAdapter.setListWashType(listData)
                        showLoading(false)
                        vendorWashTypeAdapter.notifyDataSetChanged()

                        with(vendorServicesBinding.rvWashType) {
                            layoutManager = LinearLayoutManager(requireContext())
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = vendorWashTypeAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(requireContext(), R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            vendorServicesBinding.progressBar.visibility = View.VISIBLE
        } else {
            vendorServicesBinding.progressBar.visibility = View.GONE
        }
    }
}