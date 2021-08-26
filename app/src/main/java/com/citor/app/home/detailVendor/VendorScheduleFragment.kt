package com.citor.app.home.detailVendor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.citor.app.R
import com.citor.app.databinding.FragmentVendorScheduleBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorScheduleFragment : Fragment() {

    private lateinit var vendorScheduleBinding: FragmentVendorScheduleBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vendorScheduleBinding = FragmentVendorScheduleBinding.inflate(layoutInflater, container, false)
        return vendorScheduleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPreferences = MySharedPreferences(requireContext())
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val id = arguments?.getString("id").toString()
        getJamMitra(id, tokenAuth)
    }

    private fun getJamMitra(idmitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getJamMitra(idmitra, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val jam = response.body()!!.data[0].jamMitra
                        vendorScheduleBinding.valueMondayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueTuesdayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueWednesdayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueThursdayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueFridayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueSaturdayHour.text = "$jam WIB"
                        vendorScheduleBinding.valueSundayHour.text = "$jam WIB"
                    }
                }
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(requireContext(), R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}