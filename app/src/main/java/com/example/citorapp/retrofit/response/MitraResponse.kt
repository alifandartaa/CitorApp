package com.example.citorapp.retrofit.response

import com.example.citorapp.home.searchVendor.VendorItemEntity

class MitraResponse(
    var status: String = "",
    var data: ArrayList<VendorItemEntity>
)