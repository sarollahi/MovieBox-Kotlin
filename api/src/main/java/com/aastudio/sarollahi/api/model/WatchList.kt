/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable

data class WatchList(
    val id: String? = null,
    val originalLanguage: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val rating: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val type: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(originalLanguage)
        parcel.writeString(overview)
        parcel.writeString(posterPath)
        parcel.writeString(rating)
        parcel.writeString(releaseDate)
        parcel.writeString(title)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WatchList> {
        override fun createFromParcel(parcel: Parcel): WatchList {
            return WatchList(parcel)
        }

        override fun newArray(size: Int): Array<WatchList?> {
            return arrayOfNulls(size)
        }
    }
}
