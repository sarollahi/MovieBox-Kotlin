/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.util

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class FcmTokenRegistrationService : IntentService("FcmTokenRegistrationService") {
    private val tag = "FcmTokenRegistrationService"
    override fun onHandleIntent(intent: Intent?) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(tag, "Firebase getInstanceId failed " + task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result
                    Log.d(tag, "Firebase registrationToken=$token")
                }
            )
    }
}
