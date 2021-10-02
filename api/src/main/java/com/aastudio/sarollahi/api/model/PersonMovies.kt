/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PersonMovies(
    @SerializedName("cast") val castList: List<Movie>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Movie)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(castList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonMovies> {
        override fun createFromParcel(parcel: Parcel): PersonMovies {
            return PersonMovies(parcel)
        }

        override fun newArray(size: Int): Array<PersonMovies?> {
            return arrayOfNulls(size)
        }
    }
}
