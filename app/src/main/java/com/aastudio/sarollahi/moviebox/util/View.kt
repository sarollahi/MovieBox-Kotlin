/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.util

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.ViewAlertBinding

fun createDialog(
    context: Context,
    title: String,
    message: String,
    yes: (view: AlertDialog) -> Unit,
    no: (view: AlertDialog) -> Unit
) {
    val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        .create()
    builder.setCanceledOnTouchOutside(false)
    val view: ViewAlertBinding = ViewAlertBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )
    builder.setView(view.root)
    view.title.text = title
    view.message.text = message
    view.yes.setOnClickListener {
        yes(builder)
        // startActivity(context, Intent(context, activity::class.java))
        builder.dismiss()
    }
    view.no.setOnClickListener {
        no(builder)
        builder.dismiss()
    }
    builder.setCanceledOnTouchOutside(true)
    builder.setCancelable(false)
    builder.show()
}
