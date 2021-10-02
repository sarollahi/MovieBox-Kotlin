/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.watchList.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.WatchList
import com.aastudio.sarollahi.common.CombinedLiveData
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.WATCHLIST_ERROR
import com.aastudio.sarollahi.common.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WatchListViewModel(application: Application) : ViewModel() {
    private val context = application
    val movies = MutableLiveData<ArrayList<WatchList>>()
    val shows = MutableLiveData<ArrayList<WatchList>>()
    var loading = MutableLiveData<Boolean>()

    val watchList = CombinedLiveData(shows, movies) { shows, movies ->
        hashMapOf(Pair("movie", movies), Pair("show", shows))
    }

    fun getWatchList() {
        val movieArrayList: ArrayList<WatchList> = arrayListOf()
        val showArrayList: ArrayList<WatchList> = arrayListOf()
        val databaseReference = FirebaseDatabase.getInstance().getReference("watchList")
            .child("${FirebaseUtils.firebaseAuth.currentUser?.uid}")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val item = userSnapshot.getValue(WatchList::class.java)
                        item?.let {
                            when (item.type) {
                                "Show" -> showArrayList.add(item)
                                "Movie" -> movieArrayList.add(item)
                                else -> {
                                }
                            }
                        }
                    }
                    movies.value = movieArrayList
                    shows.value = showArrayList
                    loading.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val bundle = Bundle()
                bundle.putString("Error", error.message)
                logEvent(context, WATCHLIST_ERROR, bundle)
            }
        })
    }
}
