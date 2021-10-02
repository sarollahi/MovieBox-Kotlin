/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PersonTVShows(
    @SerializedName("cast") val castList: List<TVShow>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(TVShow)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(castList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonTVShows> {
        override fun createFromParcel(parcel: Parcel): PersonTVShows {
            return PersonTVShows(parcel)
        }

        override fun newArray(size: Int): Array<PersonTVShows?> {
            return arrayOfNulls(size)
        }
    }
}
