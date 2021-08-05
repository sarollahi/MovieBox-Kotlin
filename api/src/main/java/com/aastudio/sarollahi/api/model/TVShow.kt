/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVShow(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genres") val genre: List<Genre>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("origin_country") val country: List<String>?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("torrents") val torrents: List<BaseMovie.Torrent>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Genre),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(BaseMovie.Torrent.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(firstAirDate)
        parcel.writeString(backdropPath)
        parcel.writeTypedList(genre)
        parcel.writeString(originalLanguage)
        parcel.writeStringList(country)
        parcel.writeString(overview)
        parcel.writeString(posterPath)
        parcel.writeValue(rating)
        parcel.writeValue(voteCount)
        parcel.writeTypedList(torrents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVShow> {
        override fun createFromParcel(parcel: Parcel): TVShow {
            return TVShow(parcel)
        }

        override fun newArray(size: Int): Array<TVShow?> {
            return arrayOfNulls(size)
        }
    }
}
