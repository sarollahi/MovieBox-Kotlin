/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.personDetails

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.moviebox.R
import retrofit2.Call

class PersonViewModel(private val application: Application) : ViewModel() {

    val personInfo = MutableLiveData<Person>()

    fun getPersonDetails(personId: Int) {
        Repository.getPersonDetails(
            personId,
            ::onPersonDetailsFetched,
            ::onError
        )
    }

    private fun onPersonDetailsFetched(
        person: Person
    ) {
        personInfo.value = person
    }

    private fun onError(call: Call<Person?>, error: String) {
        Toast.makeText(
            application,
            application.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }
}
