/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aastudio.sarollahi.moviebox.Resource
import com.masterwok.opensubtitlesandroid.OpenSubtitlesUrlBuilder
import com.masterwok.opensubtitlesandroid.models.OpenSubtitleItem
import com.masterwok.opensubtitlesandroid.services.OpenSubtitlesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamViewModel(private val service: OpenSubtitlesService) :
    ViewModel() {

    private val subtitleStatus = MutableLiveData<Resource<Uri>>()
    private val subtitlesData = MutableLiveData<Resource<Array<OpenSubtitleItem>>>()
    fun getSubtitleStatus() = subtitleStatus as LiveData<Resource<Uri>>
    fun getSubtitlesData() = subtitlesData as LiveData<Resource<Array<OpenSubtitleItem>>>

    fun searchMovieSubtitle(movieName: String) {
        subtitlesData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val url = OpenSubtitlesUrlBuilder()
                .query(movieName)
                .build()
            val result =
                kotlin.runCatching { service.search(OpenSubtitlesService.TemporaryUserAgent, url) }
            result.onSuccess { subtitlesData.postValue(Resource.Loaded(result.getOrThrow())) }
            result.onFailure {
                subtitlesData.postValue(
                    Resource.Error(
                        "An error occurred, Please try again..",
                        null
                    )
                )
            }
        }
    }

    fun downloadSubtitle(context: Context, subtitle: OpenSubtitleItem, filePath: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = kotlin.runCatching {
                service.downloadSubtitle(
                    context,
                    subtitle,
                    filePath
                )
            }
            result.onSuccess {
                subtitleStatus.postValue(Resource.Loaded(filePath))
            }
            result.onFailure {
                subtitleStatus.postValue(Resource.Error(it.toString(), null))
            }
        }
    }
}
