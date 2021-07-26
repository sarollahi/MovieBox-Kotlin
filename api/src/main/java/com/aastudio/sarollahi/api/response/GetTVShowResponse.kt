/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.response

import com.aastudio.sarollahi.api.model.TVShow
import com.google.gson.annotations.SerializedName

data class GetTVShowResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val tvShows: List<TVShow>,
    @SerializedName("total_pages") val pages: Int
)
