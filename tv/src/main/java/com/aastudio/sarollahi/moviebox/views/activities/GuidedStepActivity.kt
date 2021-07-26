/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import com.aastudio.sarollahi.moviebox.views.fragments.ChooseStartPositionFragment

class GuidedStepActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null == savedInstanceState) {
            GuidedStepSupportFragment.add(supportFragmentManager, ChooseStartPositionFragment())
        }
    }
}
