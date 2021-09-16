/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class BaseMovie {
    @SerializedName("status")
    val status: String? = null
    @SerializedName("status_message")
    val statusMessage: String? = null
    @SerializedName("data")
    val data: Data? = null

    class Torrent() : Parcelable {

        @SerializedName("url")
        val url: String? = null
        @SerializedName("quality")
        val quality: String? = null
        @SerializedName("type")
        val type: String? = null
        @SerializedName("size")
        val size: String? = null

        constructor(parcel: Parcel) : this()

        override fun writeToParcel(parcel: Parcel, flags: Int) {
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Torrent> {
            override fun createFromParcel(parcel: Parcel): Torrent {
                return Torrent(parcel)
            }

            override fun newArray(size: Int): Array<Torrent?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Movie {
        @SerializedName("torrents")
        val torrents: List<Torrent>? = null
    }

    class Data {
        @SerializedName("movies")
        val movies: List<Movie>? = null
    }
}
