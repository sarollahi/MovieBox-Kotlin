/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.response

import com.aastudio.sarollahi.api.model.Person
import com.google.gson.annotations.SerializedName

data class GetPersonResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val people: List<Person>,
    @SerializedName("total_pages") val pages: Int
)
