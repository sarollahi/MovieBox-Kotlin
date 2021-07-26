/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.ErrorSupportFragment
import com.aastudio.sarollahi.moviebox.R

class ErrorViewFragment : ErrorSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Oooops :("
        imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.lb_ic_sad_cloud)
        message = "NO Internet connection!"
        setDefaultBackground(false)
        buttonText = "Back to home"
        buttonClickListener = View.OnClickListener {
            activity?.finish()
        }
    }
}
