/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetPersonResponse
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.PERSON_ERROR
import retrofit2.Call

class PersonViewModel(application: Application) : ViewModel() {

    private val context = application
    val personInfo = MutableLiveData<Person>()
    val people = MutableLiveData<List<Person>>()

    fun searchPeople(name: String) {
        Repository.searchPerson(
            query = name,
            onSuccess = ::onPersonFetched,
            onError = ::onPersonError
        )
    }

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

    private fun onPersonFetched(list: List<Person>) {
        people.value = list
    }

    private fun onError(call: Call<Person?>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, PERSON_ERROR, bundle)
    }

    private fun onPersonError(call: Call<GetPersonResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, PERSON_ERROR, bundle)
    }
}
