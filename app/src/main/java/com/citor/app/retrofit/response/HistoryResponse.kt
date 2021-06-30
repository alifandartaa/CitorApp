package com.example.citorapp.retrofit.response

import com.citor.app.history.HistoryEntity

class HistoryResponse(
    var status: String = "",
    var data: ArrayList<HistoryEntity>
)