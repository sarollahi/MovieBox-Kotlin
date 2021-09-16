/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.common

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

fun logEvent(context: Context, eventName: String, params: Bundle) {
    FirebaseAnalytics.getInstance(context).logEvent(eventName, params)
}
