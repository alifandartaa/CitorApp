package com.example.appmitra.retrofit.response

import com.example.appmitra.auth.UserEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<UserEntity>,
    var tokenAuth: String = ""
)

