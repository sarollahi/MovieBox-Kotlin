package com.aastudio.sarollahi.api.response

import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.google.gson.annotations.SerializedName

data class GetTVShowResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val tvShows: List<TVShow>,
    @SerializedName("total_pages") val pages: Int
)
