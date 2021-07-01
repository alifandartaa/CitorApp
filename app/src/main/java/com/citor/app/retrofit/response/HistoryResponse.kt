package com.citor.app.retrofit.response

import com.citor.app.history.HistoryEntity

class HistoryResponse(
    var status: String = "",
    var data: ArrayList<HistoryEntity>
)