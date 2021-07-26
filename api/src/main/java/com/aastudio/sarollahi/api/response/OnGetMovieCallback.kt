/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.response

import com.aastudio.sarollahi.api.model.Movie

interface OnGetMovieCallback {
    fun onSuccess(movie: Movie)
    fun onError()
}
