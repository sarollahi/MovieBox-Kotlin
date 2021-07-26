/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProfileImages(
    @SerializedName("profiles") val profiles: List<Images>?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Images)?.toList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(profiles)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileImages> {
        override fun createFromParcel(parcel: Parcel): ProfileImages {
            return ProfileImages(parcel)
        }

        override fun newArray(size: Int): Array<ProfileImages?> {
            return arrayOfNulls(size)
        }
    }
}

data class Images(
    @SerializedName("file_path") val path: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Images> {
        override fun createFromParcel(parcel: Parcel): Images {
            return Images(parcel)
        }

        override fun newArray(size: Int): Array<Images?> {
            return arrayOfNulls(size)
        }
    }
}
