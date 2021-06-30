package com.citor.app.retrofit.response

import com.citor.app.home.searchVendor.VendorItemEntity

class MitraResponse(
    var status: String = "",
    var data: ArrayList<VendorItemEntity>
)