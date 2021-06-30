package com.example.citorapp.retrofit.response

import com.example.citorapp.history.HistoryEntity

class HistoryResponse(
    var status: String = "",
    var data: ArrayList<HistoryEntity>
)