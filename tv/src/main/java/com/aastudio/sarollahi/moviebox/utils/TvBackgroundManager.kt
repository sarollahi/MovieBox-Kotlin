/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import com.aastudio.sarollahi.moviebox.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class TvBackgroundManager(private val activity: Activity) {
    var backgroundManager: BackgroundManager = BackgroundManager.getInstance(activity).apply {
        attach(activity.window)
    }
    var defaultDrawable: Drawable = ContextCompat.getDrawable(activity, R.drawable.iodroid_main_bg)!!

    fun updateBackground(drawable: Drawable) {
        backgroundManager.drawable = drawable
    }

    fun updateBackground(imageUrl: String) {
        Glide.with(activity)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    backgroundManager.setBitmap(resource)
                }
            })
    }

    fun clearBackground() {
        backgroundManager.drawable = defaultDrawable
    }
}
