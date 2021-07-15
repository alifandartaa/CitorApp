package com.citor.app.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.NotificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel : ViewModel() {

    private val listNotification = MutableLiveData<ArrayList<NotifEntity>>()

    fun loadListNotificationfromDatabase(idUser: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getNotification(idUser, "Bearer $tokenAuth").enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val listData = response.body()!!.data
                        listNotification.postValue(listData)
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getListDataNotification(): LiveData<ArrayList<NotifEntity>> {
        return listNotification
    }
}