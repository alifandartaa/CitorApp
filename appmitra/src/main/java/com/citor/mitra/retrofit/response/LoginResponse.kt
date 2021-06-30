package com.citor.mitra.retrofit.response

import com.citor.mitra.auth.UserEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<UserEntity>,
    var tokenAuth: String = ""
)

