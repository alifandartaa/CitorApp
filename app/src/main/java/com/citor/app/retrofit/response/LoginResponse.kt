package com.citor.app.retrofit.response

import com.citor.app.auth.UserEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<UserEntity>,
    var tokenAuth: String = ""
)

