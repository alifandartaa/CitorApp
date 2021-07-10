package com.citor.mitra.retrofit

import com.citor.mitra.retrofit.response.DefaultResponse
import com.citor.mitra.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    //login
    @FormUrlEncoded
    @POST("auth_mitra/login")
    fun login(
        @Field("nohp") nohp: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    //precheck email
    @FormUrlEncoded
    @POST("auth_mitra/precheckEmail")
    fun precheck(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //register
    @FormUrlEncoded
    @POST("auth_mitra/register")
    fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("nohp") nohp: String,
        @Field("password") password: String,
    ): Call<DefaultResponse>

    //request OTP
    @FormUrlEncoded
    @POST("auth_mitra/requestOtp")
    fun requestOtp(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //verify OTP
    @FormUrlEncoded
    @POST("auth_mitra/verifyOtp")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("auth_mitra/addToken")
    fun addToken(
        @Field("idmitra") idmitra: String,
        @Field("device_token") device_token: String,
    ): Call<DefaultResponse>

    //refresh auth token
    @FormUrlEncoded
    @POST("auth_mitra/refreshToken")
    fun refreshAuthToken(
        @Field("idmitra") idmitra: String
    ): Call<LoginResponse>


}