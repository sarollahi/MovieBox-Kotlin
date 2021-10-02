/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.contact

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.common.util.FirebaseUtils
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.ActivityContactBinding
import com.aastudio.sarollahi.moviebox.util.createDialog
import com.google.firebase.database.FirebaseDatabase

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private lateinit var contactInputsArray: Array<EditText>

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "Email"
        private const val MODEL = "Model"
        private const val COUNTRY = "Country"
        private const val MESSAGE = "Message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactInputsArray = arrayOf(binding.name, binding.email, binding.message)

        binding.submit.setOnClickListener {

            if (notEmpty()) {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("Contact").child(
                        FirebaseUtils.firebaseAuth.currentUser?.uid
                            ?: "${binding.name.text} (${System.currentTimeMillis()})"
                    ).push()
                val values = HashMap<String, String>()
                values[NAME] = binding.name.text.toString()
                values[EMAIL] = binding.email.text.toString()
                values[MODEL] =
                    "${Build.MANUFACTURER} (${Build.MODEL}) - API: ${Build.VERSION.SDK_INT}"
                values[COUNTRY] =
                    (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simCountryIso
                    ?: (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso
                values[MESSAGE] = binding.message.text.toString()
                ref.setValue(values)
                createDialog(
                    this,
                    getString(R.string.contact_alert_title),
                    getString(R.string.contact_alert_message),
                    {
                        finish()
                    },
                    {
                        reset()
                        it.dismiss()
                    }
                )
            } else {
                contactInputsArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        when (input) {
                            binding.name -> input.error = getString(R.string.error_name)
                            binding.email -> input.error = getString(R.string.error_email)
                            binding.message -> input.error = getString(R.string.error_message)
                        }
                    }
                }
            }
        }
    }

    private fun notEmpty(): Boolean =
        binding.name.text.toString().trim().isNotEmpty() && binding.email.text.toString().trim()
            .isNotEmpty() && binding.message.text.toString().trim().isNotEmpty()

    private fun reset() {
        binding.name.setText("")
        binding.email.setText("")
        binding.message.setText("")
    }
}
