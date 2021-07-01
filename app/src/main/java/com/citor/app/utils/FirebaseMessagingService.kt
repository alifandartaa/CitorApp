package com.citor.app.utils

import android.annotation.SuppressLint
import com.citor.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
//        val tes = remoteMessage.data["title"]

        val tipe = remoteMessage.data["title"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["message"]
//        when (tipe) {
//            "vaksin" -> {
//                title = getString(R.string.notif_title_vaksin)
//                body = getString(R.string.notif_body_vaksin)
//            }
//            "vitamin" -> {
//                title = getString(R.string.notif_title_vitamin)
//                body = getString(R.string.notif_body_vitamin)
//            }
//            "reproduksi" -> {
//                title = getString(R.string.notif_title_reproduksi)
//                body = getString(R.string.notif_body_reproduksi)
//            }
//            "groom" -> {
//                title = getString(R.string.notif_title_groom)
//                body = getString(R.string.notif_body_groom)
//            }
//            "anthel" -> {
//                title = getString(R.string.notif_title_anthel)
//                body = getString(R.string.notif_body_anthel)
//            }
//        }
        NotificationHelper(applicationContext).displayNotification(title!!, body!!)
    }

}