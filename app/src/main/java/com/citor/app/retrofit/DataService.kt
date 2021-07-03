package com.citor.app.retrofit

import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.HistoryResponse
import com.citor.app.retrofit.response.MitraResponse
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

    @FormUrlEncoded
    @POST("main/getPemesanan")
    fun getPemesanan(
        @Field("iduser") iduser: String,
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @FormUrlEncoded
    @POST("main/insertPemesanan")
    fun insertPemesanan(
        @Field("idmitra") idmitra: String,
        @Field("iduser") iduser: String,
        @Field("idjam_buka") idjam_buka: String,
        @Field("kodePesan") kodePesan: String,
        @Field("metodeBayar") metodeBayar: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/changeStatus")
    fun changeStatus(
        @Field("idjam_buka") idjam_buka: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>
}