package com.aastudio.sarollahi.moviebox.views.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.views.fragments.ErrorViewFragment


class ErrorActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val mErrorFragment = ErrorViewFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_error_root, mErrorFragment)
            .commit()
    }
}
