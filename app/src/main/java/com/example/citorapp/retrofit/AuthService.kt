package com.example.citorapp.retrofit

import com.example.citorapp.retrofit.response.DefaultResponse
import com.example.citorapp.retrofit.response.LoginResponse
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