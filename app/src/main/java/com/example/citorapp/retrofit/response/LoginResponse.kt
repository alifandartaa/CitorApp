package com.example.citorapp.retrofit.response

import com.example.citorapp.model.MUser

class LoginResponse(
    var status: String = "",
    var data: ArrayList<MUser>,
    var tokenAuth: String = ""
)

