package com.citor.app.retrofit

import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    //login
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    //get Foto User
    @FormUrlEncoded
    @POST("auth/getUserFoto")
    fun getUserFoto(
        @Field("iduser") iduser: String
    ): Call<LoginResponse>

    //precheck email
    @FormUrlEncoded
    @POST("auth/precheckEmail")
    fun precheck(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //register
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("nohp") nohp: String,
        @Field("password") password: String,
    ): Call<DefaultResponse>

    //request OTP
    @FormUrlEncoded
    @POST("auth/requestOtp")
    fun requestOtp(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //verify OTP
    @FormUrlEncoded
    @POST("auth/verifyOtp")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("auth/addToken")
    fun addToken(
        @Field("iduser") iduser: String,
        @Field("device_token") device_token: String,
    ): Call<DefaultResponse>

    //refresh auth token
    @FormUrlEncoded
    @POST("auth/refreshToken")
    fun refreshAuthToken(
        @Field("iduser") iduser: String
    ): Call<LoginResponse>
}