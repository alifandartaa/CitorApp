package com.citor.mitra.retrofit

import com.citor.mitra.retrofit.response.DefaultResponse
import com.citor.mitra.retrofit.response.MitraResponse
import com.citor.mitra.retrofit.response.ServiceResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface DataService {
    //get data mitra
    @POST("main_mitra/getMitra")
    fun getMitra(
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get data layanan mitra
    @FormUrlEncoded
    @POST("main_mitra/getLayanan")
    fun getLayanan(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get jam buka mitra
    @FormUrlEncoded
    @POST("main_mitra/getJamBuka")
    fun getJamBuka(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get status buka mitra
    @FormUrlEncoded
    @POST("main_mitra/getStatusBuka")
    fun getStatusBuka(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //change status buka mitra
    @FormUrlEncoded
    @POST("main_mitra/bukaTutup")
    fun changeBukaTutup(
        @Field("idmitra") idmitra: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get total pesanan hari ini
    @FormUrlEncoded
    @POST("main_mitra/getTotalPesanan")
    fun getTotalPesanan(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    //get pemesanan
    @FormUrlEncoded
    @POST("main_mitra/getPemesanan")
    fun getPemesanan(
        @Field("idmitra") idmitra: String,
        @Header("Authorization") token: String
    ): Call<ServiceResponse>
}