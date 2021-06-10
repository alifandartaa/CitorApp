package com.example.citorapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun apiRequest(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://test.icaapps.online/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}