/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.contact

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.ActivityContactBinding
import com.aastudio.sarollahi.moviebox.databinding.ViewAlertBinding
import com.google.firebase.database.FirebaseDatabase

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "Email"
        private const val MODEL = "Model"
        private const val MESSAGE = "Message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {

            when {
                binding.name.text.toString().trim() == "" -> {
                    binding.emptyName.visibility = View.VISIBLE
                }
                binding.email.text.toString().trim() == "" -> {
                    binding.emptyEmail.visibility = View.VISIBLE
                }
                binding.message.text.toString().trim() == "" -> {
                    binding.emptyMessage.visibility = View.VISIBLE
                }
                else -> {
                    val ref = FirebaseDatabase.getInstance()
                        .getReference("${binding.name.text} (${System.currentTimeMillis()})").push()
                    val values = HashMap<String, String>()
                    values[NAME] = binding.name.text.toString()
                    values[EMAIL] = binding.email.text.toString()
                    values[MODEL] = "${Build.MANUFACTURER} (${Build.MODEL})"
                    values[MESSAGE] = binding.message.text.toString()
                    ref.setValue(values)
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        builder.setCanceledOnTouchOutside(false)
        val view: ViewAlertBinding = ViewAlertBinding.inflate(
            LayoutInflater.from(this),
            null,
            false
        )
        builder.setView(view.root)
        view.yes.setOnClickListener {
            finish()
        }
        view.no.setOnClickListener {
            reset()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.setCancelable(false)
        builder.show()
    }

    private fun reset() {
        binding.emptyName.visibility = View.INVISIBLE
        binding.emptyEmail.visibility = View.INVISIBLE
        binding.emptyMessage.visibility = View.INVISIBLE

        binding.name.setText("")
        binding.email.setText("")
        binding.message.setText("")
    }
}
