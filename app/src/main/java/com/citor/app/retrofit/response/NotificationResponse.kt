package com.citor.app.retrofit.response

import com.citor.app.notification.NotifEntity

class NotificationResponse(
    var status: String = "",
    var data: ArrayList<NotifEntity>
)