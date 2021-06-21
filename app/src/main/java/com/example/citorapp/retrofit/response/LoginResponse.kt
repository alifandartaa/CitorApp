package com.example.citorapp.retrofit.response

import com.example.citorapp.auth.UserEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<UserEntity>,
    var tokenAuth: String = ""
)

