package com.aastudio.sarollahi.moviebox.ui.personDetails

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.R

class PersonViewModel(private val application: Application) : ViewModel() {

    val personInfo = MutableLiveData<Person>()

    fun getPersonDetails(personId: Int) {
        MoviesRepository.getPersonDetails(
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

    private fun onError() {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }
}
