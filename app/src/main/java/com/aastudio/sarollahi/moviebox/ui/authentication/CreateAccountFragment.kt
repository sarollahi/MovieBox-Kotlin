/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aastudio.sarollahi.common.util.FirebaseUtils.firebaseAuth
import com.aastudio.sarollahi.common.util.FirebaseUtils.firebaseUser
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.FragmentCreateAccountBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var createAccountInputsArray: Array<EditText>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountInputsArray =
            arrayOf(binding.email, binding.password, binding.passwordConfirmation)
        binding.signup.setOnClickListener {
            signUp()
        }

        binding.signin.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun notEmpty(): Boolean = binding.email.text.toString().trim().isNotEmpty() &&
        binding.password.text.toString().trim().isNotEmpty() &&
        binding.passwordConfirmation.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            binding.password.text.toString().trim() == binding.passwordConfirmation.text.toString()
                .trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    when (input) {
                        binding.email -> input.error = getString(R.string.error_email)
                        binding.password -> input.error = getString(R.string.error_password)
                        binding.passwordConfirmation ->
                            input.error =
                                getString(R.string.error_password_confirmation)
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "passwords are not matching!", Toast.LENGTH_LONG)
                .show()
        }
        return identical
    }

    private fun signUp() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = binding.email.text.toString().trim()
            userPassword = binding.password.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "created account successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        sendEmailVerification()
                        activity?.finish()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "failed to Authenticate!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "email sent to $userEmail", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
