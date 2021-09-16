/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.util

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val tag = "FirebaseMessagingService"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val val1 = data["myKey1"]
        val val2 = data["myKey2"]
        Log.d(tag, "Message received val1=$val1 val2=$val2")
        val handler = Handler(mainLooper)
        handler.post(
            Runnable {
                Toast.makeText(
                    this@MyFirebaseMessagingService.applicationContext,
                    "Message received val1=$val1 val2=$val2", Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    override fun onNewToken(registrationToken: String) {
        Log.d(tag, "Firebase #onNewToken registrationToken=$registrationToken")
        startService(Intent(this, FcmTokenRegistrationService::class.java))
    }
}
