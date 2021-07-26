/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.response

import com.aastudio.sarollahi.api.model.TVShow

interface OnGetTVShowCallback {
    fun onSuccess(tvShow: TVShow)
    fun onError()
}
