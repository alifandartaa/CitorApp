package com.citor.mitra.retrofit.response

import com.citor.mitra.VendorItemEntity

class MitraResponse(
    var status: String = "",
    var data: ArrayList<VendorItemEntity>
)