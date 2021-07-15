package com.citor.app.retrofit

import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.HistoryResponse
import com.citor.app.retrofit.response.MitraResponse
import com.citor.app.retrofit.response.NotificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    //get jam buka status
    @FormUrlEncoded
    @POST("main/getJamBukaStatus")
    fun getJamBukaStatus(
        @Field("idmitra") idmitra: String,
        @Field("idjam_buka") idjam_buka: String,
        @Header("Authorization") token: String
    ): Call<MitraResponse>

    @FormUrlEncoded
    @POST("main/getPemesanan")
    fun getPemesanan(
        @Field("iduser") iduser: String,
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @FormUrlEncoded
    @POST("main/getNotif")
    fun getNotification(
        @Field("iduser") iduser: String,
        @Header("Authorization") token: String
    ): Call<NotificationResponse>

    @FormUrlEncoded
    @POST("main/insertPemesanan")
    fun insertPemesanan(
        @Field("idmitra") idmitra: String,
        @Field("iduser") iduser: String,
        @Field("idjam_buka") idjam_buka: String,
        @Field("kodePesan") kodePesan: String,
        @Field("metodeBayar") metodeBayar: String,
        @Field("harga") harga: String,
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

    @Multipart
    @POST("main/editProfile")
    fun editprofile(
        @Part("iduser") iduser: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("email") email: RequestBody,
        @Part("nohp") nohp: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>
}