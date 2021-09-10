/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Trailers(
    @SerializedName("results") val results: List<Trailer>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Trailer))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trailers> {
        override fun createFromParcel(parcel: Parcel): Trailers {
            return Trailers(parcel)
        }

        override fun newArray(size: Int): Array<Trailers?> {
            return arrayOfNulls(size)
        }
    }
}

data class Trailer(
    @SerializedName("key") val key: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trailer> {
        override fun createFromParcel(parcel: Parcel): Trailer {
            return Trailer(parcel)
        }

        override fun newArray(size: Int): Array<Trailer?> {
            return arrayOfNulls(size)
        }
    }
}
