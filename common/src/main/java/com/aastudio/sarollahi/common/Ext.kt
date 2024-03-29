/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

@file:Suppress("DEPRECATION")

package com.aastudio.sarollahi.common

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (T) -> Unit) =
    liveData.observe(this, { observer(it as T) })

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Activity.gone() {
    this.setVisible(false)
}

fun Activity.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun SimpleExoPlayer.startPlayer(mediaSource: MergingMediaSource) {
    prepare(mediaSource)
    playWhenReady = true
    playbackState
}

fun SimpleExoPlayer.seekPlayer(
    pos: Long,
    mergeMediaSource: MergingMediaSource
) {
    startPlayer(mergeMediaSource)
    seekTo(pos)
}

fun SimpleExoPlayer.addingSubtitle(mediaSource: MergingMediaSource, position: Long) {
    prepare(mediaSource)
    resumePlayer()
    seekTo(position)
}

fun SimpleExoPlayer.resumePlayer() {
    playWhenReady = true
    playbackState
}

fun SimpleExoPlayer.stopPlayer() {
    playWhenReady = false
    playbackState
}

fun TextView.textOrGone(s: String?) {
    if (s!!.isEmpty())
        invisible()
    else {
        text = s
        show()
    }
}

fun TextView.addCategories(genres: List<String>) {
    var category = ""
    for (i in genres.indices) {
        category += genres[i]
        if (i != genres.size - 1)
            category += " | "
    }
    text = category
}

fun hideSystemUI(): Int {
    return (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
}

fun showSystemUI(): Int {
    return (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
}

fun TextView.formatText(stringPath: Int, obj1: Any?, obj2: Any? = 0) {
    text = String.format(resources.getString(stringPath), obj1, obj2)
}

fun RecyclerView.addDividers(dividerItemDecoration: Int) {
    addItemDecoration(
        DividerItemDecoration(
            context,
            if (dividerItemDecoration == DividerItemDecoration.VERTICAL) {
                DividerItemDecoration.VERTICAL
            } else {
                DividerItemDecoration.HORIZONTAL
            }
        )
    )
}

class CombinedLiveData<T, K, S>(source1: LiveData<T>, source2: LiveData<K>, private val combine: (data1: T?, data2: K?) -> S) : MediatorLiveData<S>() {

    private var data1: T? = null
    private var data2: K? = null

    init {
        super.addSource(source1) {
            data1 = it
            value = combine(data1, data2)
        }
        super.addSource(source2) {
            data2 = it
            value = combine(data1, data2)
        }
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> removeSource(toRemove: LiveData<T>) {
        throw UnsupportedOperationException()
    }
}
