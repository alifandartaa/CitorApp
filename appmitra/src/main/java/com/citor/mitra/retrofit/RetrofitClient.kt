package com.citor.mitra.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun apiRequest(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://citor.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}