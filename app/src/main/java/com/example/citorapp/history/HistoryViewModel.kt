package com.example.citorapp.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.citorapp.retrofit.DataService
import com.example.citorapp.retrofit.RetrofitClient
import com.example.citorapp.retrofit.response.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val listHistory = MutableLiveData<ArrayList<HistoryEntity>>()

    fun loadListHistoryFromDatabase(idUser: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getPemesanan(idUser, "Bearer $tokenAuth").enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data
                        listHistory.postValue(listData)
                    }
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
//                Toasty.error(this, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        }
        )
    }

    fun getListDataHistory(): LiveData<ArrayList<HistoryEntity>> {
        return listHistory
    }
}