/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.util

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory

fun inAppReview(activity: Activity) {
    val reviewManager = ReviewManagerFactory.create(activity)
    val requestReviewFlow = reviewManager.requestReviewFlow()
    requestReviewFlow.addOnCompleteListener { request ->
        if (request.isSuccessful) {
            // We got the ReviewInfo object
            val reviewInfo = request.result
            val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
            flow.addOnCompleteListener {
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown. Thus, no
                // matter the result, we continue our app flow.
            }
        } else {
            Log.d("Error: ", request.exception.toString())
            // There was some problem, continue regardless of the result.
        }
    }
}
