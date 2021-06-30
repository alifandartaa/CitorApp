package com.citor.mitra.retrofit

import com.citor.mitra.retrofit.response.MitraResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface DataService {
    //get data mitra
    @POST("main/getMitra")
    fun getMitra(
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get data layanan mitra
    @FormUrlEncoded
    @POST("main/getLayanan")
    fun getLayanan(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get jam buka mitra
    @FormUrlEncoded
    @POST("main/getJamBuka")
    fun getJamBuka(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>
}